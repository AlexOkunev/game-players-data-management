package ru.otus.courses.kafka.gameserver.util;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleConnectionEvent;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleConnectionEventType;
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

  public static String battleKey(BattleConnectionEvent event) {
    return String.valueOf(event.getBattleId());
  }

  public static long calculateTimestamp(LocalDateTime time, long amountToAdd, TemporalUnit temporalUnit) {
    return time.atZone(ZoneId.systemDefault()).plus(amountToAdd, temporalUnit).toInstant().toEpochMilli();
  }

  public static long calculateTimestamp(LocalDateTime time) {
    return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  public static BattleConnectionEvent battleConnectionStartEvent(long battleId, BattleMap map,
                                                                 LocalDateTime startTime) {
    return BattleConnectionEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setBattleId(battleId)
        .setTimestamp(calculateTimestamp(startTime))
        .setType(BattleConnectionEventType.BATTLE_STARTED)
        .setBattleInfo(BattleInfo.newBuilder()
            .setMap(map)
            .build())
        .build();
  }

  public static BattleConnectionEvent battleConnectionFinishEvent(long battleId, LocalDateTime startTime,
                                                                  long durationSeconds) {
    return BattleConnectionEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setBattleId(battleId)
        .setTimestamp(calculateTimestamp(startTime, durationSeconds, SECONDS))
        .setType(BattleConnectionEventType.BATTLE_FINISHED)
        .build();
  }

  public static BattleConnectionEvent battlePlayerConnectionEvent(long battleId, LocalDateTime startTime,
                                                                  PlayerConnection playerConnection) {
    return BattleConnectionEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setBattleId(battleId)
        .setTimestamp(calculateTimestamp(startTime, playerConnection.delaySeconds(), SECONDS))
        .setType(BattleConnectionEventType.PLAYER_CONNECTED)
        .setConnectedPlayer(ConnectedPlayer.newBuilder()
            .setPlayerId(playerConnection.playerId())
            .build())
        .build();
  }

  public static BattleEvent battleStartedEvent(long battleId, BattleMap map, LocalDateTime startTime) {
    return BattleEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setBattleId(battleId)
        .setTimestamp(calculateTimestamp(startTime))
        .setType(BattleEventType.BATTLE_STARTED)
        .setBattleInfo(BattleInfo.newBuilder()
            .setMap(map)
            .build())
        .build();
  }

  public static BattleEvent battleFinishedEvent(long battleId, List<Long> winnerIds, LocalDateTime startTime,
                                                long durationSeconds) {
    return BattleEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setBattleId(battleId)
        .setTimestamp(calculateTimestamp(startTime, durationSeconds, SECONDS))
        .setType(BattleEventType.BATTLE_FINISHED)
        .setBattleResult(BattleResult.newBuilder()
            .setWinners(winnerIds)
            .build())
        .build();
  }

  public static BattleEvent battleShotEvent(long battleId, LocalDateTime startTime, PlayerShot shot) {
    return BattleEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setBattleId(battleId)
        .setTimestamp(calculateTimestamp(startTime, shot.delaySeconds(), SECONDS))
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
}
