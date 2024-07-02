package ru.otus.courses.kafka.battle.results.service.util;

import java.util.UUID;
import lombok.experimental.UtilityClass;
import ru.otus.courses.kafka.battle.results.datatypes.PlayerBattleTotalResult;
import ru.otus.courses.kafka.battle.results.datatypes.db.PlayerBattleTotalResultRecord;
import ru.otus.courses.kafka.battle.results.datatypes.db.PlayerBattleTotalResultRecordKey;

@UtilityClass
public class DbMappingUtils {

  public static PlayerBattleTotalResultRecord mapValue(PlayerBattleTotalResult battleResult) {
    return PlayerBattleTotalResultRecord.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setMap(battleResult.getMap())
        .setBattleId(battleResult.getBattleId())
        .setBattleFinishedTimestamp(battleResult.getBattleFinishedTimestamp())
        .setPlayerId(battleResult.getPlayerId())
        .setDamageSum(battleResult.getDamageSum())
        .setShots(battleResult.getShots())
        .setSuccessfulShots(battleResult.getSuccessfulShots())
        .setHeadshots(battleResult.getHeadshots())
        .setKilled(battleResult.getKilled())
        .setDeaths(battleResult.getDeaths())
        .setWinner(battleResult.getWinner())
        .build();
  }

  public static PlayerBattleTotalResultRecordKey mapKey(PlayerBattleTotalResult battleResult) {
    return PlayerBattleTotalResultRecordKey.newBuilder()
        .setPlayerId(battleResult.getPlayerId())
        .setBattleId(battleResult.getBattleId())
        .build();
  }
}
