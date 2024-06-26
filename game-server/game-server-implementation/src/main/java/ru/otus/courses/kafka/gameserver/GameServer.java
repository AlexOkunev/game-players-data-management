package ru.otus.courses.kafka.gameserver;

import static java.lang.System.currentTimeMillis;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static ru.otus.courses.kafka.gameserver.config.Configuration.ADMIN_CONFIG;
import static ru.otus.courses.kafka.gameserver.config.Configuration.PRODUCER_CONFIG;
import static ru.otus.courses.kafka.gameserver.config.Topics.BATTLE_CONNECTION_EVENTS;
import static ru.otus.courses.kafka.gameserver.config.Topics.BATTLE_EVENTS;
import static ru.otus.courses.kafka.gameserver.config.Topics.PLAYER_BATTLE_RESULTS;
import static ru.otus.courses.kafka.gameserver.util.AdminUtils.recreateTopics;
import static ru.otus.courses.kafka.gameserver.util.BattleEventsProducerUtils.battleConnectionFinishEvent;
import static ru.otus.courses.kafka.gameserver.util.BattleEventsProducerUtils.battleConnectionStartEvent;
import static ru.otus.courses.kafka.gameserver.util.BattleEventsProducerUtils.battleFinishedEvent;
import static ru.otus.courses.kafka.gameserver.util.BattleEventsProducerUtils.battleKey;
import static ru.otus.courses.kafka.gameserver.util.BattleEventsProducerUtils.battlePlayerConnectionEvent;
import static ru.otus.courses.kafka.gameserver.util.BattleEventsProducerUtils.battleShotEvent;
import static ru.otus.courses.kafka.gameserver.util.BattleEventsProducerUtils.battleStartedEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleConnectionEvent;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleEvent;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleMap;
import ru.otus.courses.kafka.gameserver.model.PlayerConnection;
import ru.otus.courses.kafka.gameserver.model.PlayerShot;
import ru.otus.courses.kafka.gameserver.util.ProducerCallback;

@Slf4j
public class GameServer {

  public static void main(String[] args) throws InterruptedException {
//    recreateTopics(ADMIN_CONFIG, 2, 3, BATTLE_EVENTS, BATTLE_CONNECTION_EVENTS, PLAYER_BATTLE_RESULTS);

    //TODO randomize battle
    Thread.sleep(5000);

    List<PlayerConnection> playerConnections = List.of(
        new PlayerConnection(0, 0), new PlayerConnection(1, 0), new PlayerConnection(2, 10),
        new PlayerConnection(3, 35), new PlayerConnection(4, 64), new PlayerConnection(5, 72)
    );

    //TEAMS - [0, 2, 4], [1, 3, 5]
    List<PlayerShot> playersShots = List.of(
        new PlayerShot(0, 1L, 1, 10, false, false, 10),    //1 - 90 hp
        new PlayerShot(1, 2L, 1, 40, true, false, 12),     //2 - 60 hp
        new PlayerShot(2, null, 1, 0, false, false, 13),
        new PlayerShot(2, null, 1, 0, false, false, 15),
        new PlayerShot(0, 3L, 1, 25, false, false, 37),    //3 - 75 hp
        new PlayerShot(0, 1L, 2, 60, true, false, 46),     //1 - 30 hp
        new PlayerShot(0, null, 1, 0, false, false, 47),
        new PlayerShot(1, 0L, 2, 40, false, false, 48),    //0 - 60 hp
        new PlayerShot(1, 2L, 2, 40, false, false, 50),    //2 - 20 hp
        new PlayerShot(1, null, 2, 0, false, false, 51),
        new PlayerShot(0, 1L, 2, 60, true, true, 52),      //1 - dead
        new PlayerShot(3, null, 1, 0, false, false, 59),
        new PlayerShot(3, 2L, 1, 35, true, true, 60),      //2 - dead
        new PlayerShot(3, 4L, 1, 15, false, false, 70),    //4 - 85 hp
        new PlayerShot(4, 5L, 1, 37, true, false, 75),     //5 - 63 hp
        new PlayerShot(5, 4L, 2, 65, true, false, 78),     //4 - 20 hp
        new PlayerShot(5, null, 2, 0, false, false, 79),
        new PlayerShot(5, 4L, 2, 65, true, true, 80),      //4 - dead
        new PlayerShot(0, 3L, 1, 40, true, false, 89),     //3 - 35 hp
        new PlayerShot(0, null, 1, 0, false, false, 90),
        new PlayerShot(0, 3L, 1, 40, true, true, 91),      //3 - dead
        new PlayerShot(5, 0L, 2, 25, false, false, 98),    //0 - 35 hp
        new PlayerShot(5, null, 2, 0, false, false, 99),
        new PlayerShot(0, 5L, 2, 40, true, false, 100),    //5 - 23hp
        new PlayerShot(0, null, 2, 0, false, false, 101),
        new PlayerShot(5, 0L, 2, 42, true, true, 102)      //0 - dead
    );

    //Incorrect events
    List<PlayerShot> shotsBeforeStart = List.of(
        new PlayerShot(0, null, 1, 0, false, false, -10),
        new PlayerShot(1, null, 1, 0, false, false, -6),
        new PlayerShot(2, null, 1, 0, false, false, -1)
    );

    List<PlayerShot> shotsAfterFinish = List.of(
        new PlayerShot(0, null, 1, 0, false, false, 103),
        new PlayerShot(0, null, 1, 0, false, false, 104)
    );

    List<PlayerShot> delayedShots = List.of(
        new PlayerShot(4, null, 1, 0, false, false, 65),
        new PlayerShot(4, null, 1, 0, false, false, 67),
        new PlayerShot(4, null, 1, 0, false, false, 69),
        new PlayerShot(4, null, 1, 0, false, false, 70),
        new PlayerShot(4, null, 1, 0, false, false, 71),
        new PlayerShot(4, null, 1, 0, false, false, 73),
        new PlayerShot(4, null, 1, 0, false, false, 75),
        new PlayerShot(4, null, 1, 0, false, false, 76),
        new PlayerShot(4, null, 1, 0, false, false, 77),
        new PlayerShot(4, null, 1, 0, false, false, 79)
    );

    int battleDurationSeconds = playersShots.stream().mapToInt(PlayerShot::delaySeconds).max().orElse(0);

    LocalDateTime battleStartTime = LocalDateTime.now()
        .minusSeconds(shotsBeforeStart.stream().mapToInt(PlayerShot::delaySeconds).min().orElse(0));

    Random random = new Random(currentTimeMillis());
    long battleId = random.nextLong(10000);
    BattleMap battleMap = BattleMap.DE_DUST2;

    BattleConnectionEvent connectionStartEvent = battleConnectionStartEvent(battleId, battleMap, battleStartTime);
    BattleConnectionEvent connectionFinishEvent = battleConnectionFinishEvent(battleId, battleStartTime,
        battleDurationSeconds);

    Stream<BattleConnectionEvent> connectionEventsStream = playerConnections.stream()
        .map(playerConnection -> battlePlayerConnectionEvent(battleId, battleStartTime, playerConnection));

    BattleEvent battleStartedEvent = battleStartedEvent(battleId, battleMap, battleStartTime);
    BattleEvent battleFinishedEvent = battleFinishedEvent(battleId, battleStartTime, battleDurationSeconds);

    Stream<BattleEvent> battleShotsStream =
        Stream.of(shotsBeforeStart.stream(), playersShots.stream(), shotsAfterFinish.stream())
            .flatMap(identity())
            .map(shot -> battleShotEvent(battleId, battleStartTime, shot));

    List<BattleEvent> delayedBattleEvents =
        delayedShots.stream()
            .map(shot -> battleShotEvent(battleId, battleStartTime, shot))
            .toList();

    log.info("Create producer");

    try (var battleEventsProducer = new KafkaProducer<String, BattleEvent>(PRODUCER_CONFIG);
        var battleConnectionEventsProducer = new KafkaProducer<String, BattleConnectionEvent>(PRODUCER_CONFIG);
        var executorService = Executors.newFixedThreadPool(3)) {
      log.info("Send battle events");

      List<BattleEvent> battleEvents =
          Stream.of(Stream.of(battleStartedEvent), battleShotsStream, Stream.of(battleFinishedEvent))
              .flatMap(identity())
              .sorted(comparing(BattleEvent::getTimestamp))
              .toList();

      List<BattleConnectionEvent> connectionEvents =
          Stream.of(Stream.of(connectionStartEvent), connectionEventsStream, Stream.of(connectionFinishEvent))
              .flatMap(identity())
              .sorted(comparing(BattleConnectionEvent::getTimestamp))
              .toList();

      long initialTimestamp = battleEvents.getFirst().getTimestamp();

      executorService.invokeAll(List.of(
          () -> sendBattleEvents(battleEvents, battleEventsProducer, initialTimestamp),
          () -> sendBattleEventsAfter(delayedBattleEvents, battleEventsProducer, battleFinishedEvent.getTimestamp(), 9),
          () -> sendConnectionEvents(connectionEvents, battleConnectionEventsProducer, initialTimestamp)));

      log.info("Finish");
    }
  }

  private static Void sendBattleEvents(List<BattleEvent> battleEvents,
                                       KafkaProducer<String, BattleEvent> producer,
                                       long initialTime) {
    try {
      long previousEventTimestamp = initialTime;

      for (BattleEvent event : battleEvents) {
        Thread.sleep(event.getTimestamp() - previousEventTimestamp);
        log.info("Send {} event {}", event.getType(), event.getEventId());
        producer.send(new ProducerRecord<>(BATTLE_EVENTS, battleKey(event), event), ProducerCallback.INSTANCE);
        previousEventTimestamp = event.getTimestamp();
      }

      log.info("All battle events have been sent");

      return null;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private static Void sendBattleEventsAfter(List<BattleEvent> battleEvents,
                                            KafkaProducer<String, BattleEvent> producer, long initialTime,
                                            long secondsBetweenEvents) {
    try {
      long timeToWait = initialTime - currentTimeMillis();
      log.info("Wait {} seconds to send delayed {} battle events", timeToWait / 1000, battleEvents.size());
      Thread.sleep(timeToWait);

      Random random = new Random(currentTimeMillis());

      for (BattleEvent event : battleEvents) {
        long timeForSleep = random.nextLong(secondsBetweenEvents) + 1;
        log.info("Wait {} seconds to send delayed battle event", timeForSleep);
        Thread.sleep(Duration.ofSeconds(timeForSleep));
        log.info("Send delayed {} event {}. {} seconds after finish", event.getType(), event.getEventId(),
            (currentTimeMillis() - initialTime) / 1000);
        producer.send(new ProducerRecord<>(BATTLE_EVENTS, battleKey(event), event), ProducerCallback.INSTANCE);
      }

      log.info("All delayer battle events have been sent");

      return null;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private static Void sendConnectionEvents(List<BattleConnectionEvent> connectionEvents,
                                           KafkaProducer<String, BattleConnectionEvent> producer,
                                           long initialTime) {
    try {
      long previousEventTimestamp = initialTime;

      for (BattleConnectionEvent event : connectionEvents) {
        Thread.sleep(event.getTimestamp() - previousEventTimestamp);
        log.info("Send {} event {}", event.getType(), event.getEventId());
        producer.send(new ProducerRecord<>(BATTLE_CONNECTION_EVENTS, battleKey(event), event),
            ProducerCallback.INSTANCE);
        previousEventTimestamp = event.getTimestamp();
      }

      log.info("All battle connection events have been sent");

      return null;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}