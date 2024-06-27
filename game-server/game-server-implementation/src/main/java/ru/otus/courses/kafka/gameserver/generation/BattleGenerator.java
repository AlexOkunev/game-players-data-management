package ru.otus.courses.kafka.gameserver.generation;

import static java.lang.System.currentTimeMillis;
import static java.time.LocalDateTime.now;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toSet;
import static ru.otus.courses.kafka.gameserver.util.BattleEventsProducerUtils.battleConnectionFinishEvent;
import static ru.otus.courses.kafka.gameserver.util.BattleEventsProducerUtils.battleConnectionStartEvent;
import static ru.otus.courses.kafka.gameserver.util.BattleEventsProducerUtils.battleFinishedEvent;
import static ru.otus.courses.kafka.gameserver.util.BattleEventsProducerUtils.battlePlayerConnectionEvent;
import static ru.otus.courses.kafka.gameserver.util.BattleEventsProducerUtils.battleShotEvent;
import static ru.otus.courses.kafka.gameserver.util.BattleEventsProducerUtils.battleStartedEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleConnectionEvent;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleEvent;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleMap;
import ru.otus.courses.kafka.gameserver.model.GeneratedBattleData;
import ru.otus.courses.kafka.gameserver.model.PlayerConnection;
import ru.otus.courses.kafka.gameserver.model.PlayerShot;
import ru.otus.courses.kafka.gameserver.model.WeaponDamage;

@RequiredArgsConstructor
public class BattleGenerator {

  private final WeaponDamage[] weaponDamages;

  private final int maxPlayerId;

  private final int startHP;

  private final int maxBattleId;

  private final Random random = new Random(currentTimeMillis());

  public GeneratedBattleData generate(int playersCount, int shotsBeforeStartCount,
                                      int shotsAfterFinishCount, int delayedShotsCount, int maxSecondsBetweenShots,
                                      int shotSuccessProbability, int headshotProbability) {
    //Prepare

    long[] playerIds = generatePlayerIds(playersCount);

    int[] HPs = IntStream.range(0, playersCount)
        .map(i -> startHP)
        .toArray();

    long battleId = random.nextLong(maxBattleId);

    BattleMap battleMap;
    do {
      battleMap = BattleMap.values()[random.nextInt(BattleMap.values().length)];
    } while (battleMap == BattleMap.UNKNOWN);

    //Create shots and connections

    List<PlayerShot> playersShotsBeforeStart = generateShotsBeforeStart(shotsBeforeStartCount, maxSecondsBetweenShots,
        playerIds);

    List<PlayerShot> playersShots = generateShots(maxSecondsBetweenShots, shotSuccessProbability, headshotProbability,
        HPs, playerIds);

    int firstShotMoment = playersShots.getFirst().delaySeconds();
    int battleDurationSeconds = playersShots.getLast().delaySeconds();

    List<PlayerShot> playersShotsAfterFinish = generateShotsAfterFinish(shotsAfterFinishCount, maxSecondsBetweenShots,
        battleDurationSeconds, playerIds);

    List<PlayerShot> delayedShots = generateDelayedShots(delayedShotsCount, battleDurationSeconds, playerIds);

    List<PlayerConnection> playerConnections = generatePlayerConnections(playersCount, firstShotMoment, playerIds);

    List<Long> winnerIds = IntStream.range(0, playersCount)
        .filter(i -> HPs[i] > 0)
        .mapToObj(i -> playerIds[i])
        .toList();

    //Create events

    LocalDateTime battleStartTime = now().minusSeconds(playersShotsBeforeStart.getFirst().delaySeconds());

    BattleConnectionEvent connectionStartEvent = battleConnectionStartEvent(battleId, battleMap, battleStartTime);
    BattleConnectionEvent connectionFinishEvent = battleConnectionFinishEvent(battleId, battleStartTime,
        battleDurationSeconds);

    Stream<BattleConnectionEvent> connectionEventsStream = playerConnections.stream()
        .map(playerConnection -> battlePlayerConnectionEvent(battleId, battleStartTime, playerConnection));

    BattleEvent battleStartedEvent = battleStartedEvent(battleId, battleMap, battleStartTime);
    BattleEvent battleFinishedEvent = battleFinishedEvent(battleId, winnerIds, battleStartTime, battleDurationSeconds);

    Stream<BattleEvent> battleShotsStream =
        Stream.of(playersShotsBeforeStart.stream(), playersShots.stream(), playersShotsAfterFinish.stream())
            .flatMap(identity())
            .map(shot -> battleShotEvent(battleId, battleStartTime, shot));

    List<BattleEvent> battleEvents =
        Stream.of(Stream.of(battleStartedEvent), battleShotsStream, Stream.of(battleFinishedEvent))
            .flatMap(identity())
            .sorted(comparing(BattleEvent::getTimestamp))
            .toList();

    List<BattleEvent> delayedBattleEvents =
        delayedShots.stream()
            .map(shot -> battleShotEvent(battleId, battleStartTime, shot))
            .toList();

    List<BattleConnectionEvent> connectionEvents =
        Stream.of(Stream.of(connectionStartEvent), connectionEventsStream, Stream.of(connectionFinishEvent))
            .flatMap(identity())
            .sorted(comparing(BattleConnectionEvent::getTimestamp))
            .toList();

    return new GeneratedBattleData(battleEvents, delayedBattleEvents, connectionEvents);
  }

  private long[] generatePlayerIds(int playersCount) {
    long[] playerIds;
    Set<Long> playerIdsSet;

    do {
      playerIds = LongStream.range(0, playersCount)
          .map(i -> random.nextLong(1, maxPlayerId))
          .toArray();

      playerIdsSet = Arrays.stream(playerIds).boxed().collect(toSet());
    } while (playerIdsSet.size() != playersCount);
    return playerIds;
  }

  private List<PlayerConnection> generatePlayerConnections(int playersCount, int firstShotMoment, long[] playerIds) {
    return IntStream.range(0, playersCount)
        .mapToObj(i -> new PlayerConnection(playerIds[i], random.nextInt(firstShotMoment)))
        .toList();
  }

  private List<PlayerShot> generateDelayedShots(int count, int battleFinishTime, long[] playerIds) {
    return IntStream.range(0, count)
        .mapToObj(i -> createShotWithoutVictim(random.nextInt(1, battleFinishTime), playerIds))
        .toList();
  }

  private List<PlayerShot> generateShots(int maxIntervalBetweenShots, int shotSuccessProbability,
                                         int headshotProbability, int[] HPs, long[] playerIds) {
    int time = 0;
    List<PlayerShot> playerShots = new ArrayList<>();

    while (Arrays.stream(HPs).filter(hp -> hp > 0).count() > 1) {
      time += random.nextInt(1, maxIntervalBetweenShots);
      PlayerShot shot = createShot(time, shotSuccessProbability, headshotProbability, HPs, playerIds);
      performShot(shot, HPs, playerIds);
      playerShots.add(shot);
    }

    return playerShots;
  }

  private List<PlayerShot> generateShotsAfterFinish(int count, int maxIntervalBetweenShots, int battleFinishTime,
                                                    long[] playerIds) {
    int time = battleFinishTime;
    List<Integer> shotsAfterIntervals = generateShotIntervals(count, maxIntervalBetweenShots);
    List<PlayerShot> playerShots = new ArrayList<>();

    for (int interval : shotsAfterIntervals) {
      time += interval;
      playerShots.add(createShotWithoutVictim(time, playerIds));
    }

    return playerShots;
  }

  private List<PlayerShot> generateShotsBeforeStart(int count, int maxIntervalBetweenShots, long[] playerIds) {
    int time = 0;
    List<Integer> shotsBeforeIntervals = generateShotIntervals(count, maxIntervalBetweenShots);
    List<PlayerShot> playerShots = new ArrayList<>();

    for (int interval : shotsBeforeIntervals) {
      time += interval;
      playerShots.addFirst(createShotWithoutVictim(-time, playerIds));
    }

    return playerShots;
  }

  private void performShot(PlayerShot shot, int[] HPs, long[] playerIds) {
    if (shot.victimPlayerId() != null) {
      int victimNumber = IntStream.range(0, playerIds.length)
          .filter(i -> playerIds[i] == shot.victimPlayerId())
          .findFirst()
          .orElseThrow();

      HPs[victimNumber] -= shot.damage();
    }
  }

  private PlayerShot createShotWithoutVictim(int moment, long[] playerIds) {
    long shooterId = playerIds[random.nextInt(playerIds.length)];
    int weapon = random.nextInt(weaponDamages.length);
    return new PlayerShot(shooterId, null, weapon, 0, false, false, moment);
  }

  private PlayerShot createShot(int moment, int shotSuccessProbability, int headshotProbability, int[] HPs, long[] playerIds) {
    int shooterNumber;
    do {
      shooterNumber = random.nextInt(playerIds.length);
    } while (HPs[shooterNumber] <= 0);

    long shooterId = playerIds[shooterNumber];

    int weapon = random.nextInt(weaponDamages.length);

    Integer victimNumber;
    do {
      victimNumber = random.nextInt(100) <= shotSuccessProbability ? random.nextInt(playerIds.length) : null;
    } while (victimNumber != null && (HPs[victimNumber] <= 0 || shooterId == playerIds[victimNumber]));

    Long victimId = victimNumber != null ? playerIds[victimNumber] : null;

    boolean headshot = victimId != null && random.nextInt(100) <= headshotProbability;
    int damage = victimId != null ? (headshot ? weaponDamages[weapon].headshot() : weaponDamages[weapon].usual()) : 0;
    boolean killed = victimNumber != null && damage >= HPs[victimNumber];

    return new PlayerShot(shooterId, victimId, weapon, damage, headshot, killed, moment);
  }

  private List<Integer> generateShotIntervals(int count, int maxIntervalBetweenShots) {
    return IntStream.range(0, count)
        .mapToObj(i -> random.nextInt(1, maxIntervalBetweenShots))
        .toList();
  }
}
