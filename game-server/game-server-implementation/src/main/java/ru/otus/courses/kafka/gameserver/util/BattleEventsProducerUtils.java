package ru.otus.courses.kafka.gameserver.util;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleEvent;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleEventType;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleInfo;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleMap;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleResult;
import ru.otus.courses.kafka.game.server.datatypes.events.ConnectedPlayer;
import ru.otus.courses.kafka.game.server.datatypes.events.ShotInfo;
import ru.otus.courses.kafka.gameserver.model.PlayerConnection;
import ru.otus.courses.kafka.gameserver.model.PlayerShot;

@UtilityClass
public class BattleEventsProducerUtils {

  public static String battleKey(BattleEvent event) {
    return String.valueOf(event.getBattleId());
  }

  public static BattleEvent battleStartedEvent(long battleId, BattleMap map, Instant startTime) {
    return BattleEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setBattleId(battleId)
        .setTimestamp(startTime)
        .setType(BattleEventType.BATTLE_STARTED)
        .setBattleInfo(BattleInfo.newBuilder()
            .setMap(map)
            .build())
        .build();
  }

  public static BattleEvent battleFinishedEvent(long battleId, List<Long> winnerIds, Instant startTime,
                                                long durationSeconds) {
    return BattleEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setBattleId(battleId)
        .setTimestamp(startTime.plusSeconds(durationSeconds))
        .setType(BattleEventType.BATTLE_FINISHED)
        .setBattleResult(BattleResult.newBuilder()
            .setWinners(winnerIds)
            .build())
        .build();
  }

  public static BattleEvent battleShotEvent(long battleId, Instant startTime, PlayerShot shot) {
    return BattleEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setBattleId(battleId)
        .setTimestamp(startTime.plusSeconds(shot.delaySeconds()))
        .setType(BattleEventType.SHOT_PERFORMED)
        .setShotInfo(ShotInfo.newBuilder()
            .setShooterPlayerId(shot.shooterPlayerId())
            .setVictimPlayerId(shot.victimPlayerId())
            .setWeaponId(shot.weaponId())
            .setDamage(shot.damage())
            .setHeadshot(shot.headshot())
            .setKilled(shot.killed())
            .build())
        .build();
  }

  public static BattleEvent battlePlayerConnectedEvent(long battleId, Instant startTime, PlayerConnection player) {
    return BattleEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setBattleId(battleId)
        .setTimestamp(startTime.plusSeconds(player.delaySeconds()))
        .setType(BattleEventType.PLAYER_CONNECTED)
        .setConnectedPlayerInfo(ConnectedPlayer.newBuilder()
            .setPlayerId(player.playerId())
            .build())
        .build();
  }
}
