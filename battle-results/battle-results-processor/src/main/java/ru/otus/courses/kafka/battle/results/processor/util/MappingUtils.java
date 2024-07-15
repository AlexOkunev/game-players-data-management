package ru.otus.courses.kafka.battle.results.processor.util;

import java.util.UUID;
import lombok.experimental.UtilityClass;
import ru.otus.courses.kafka.battle.results.datatypes.PlayerBattleTotalResult;
import ru.otus.courses.kafka.battle.results.datatypes.WeaponResult;
import ru.otus.courses.kafka.battle.results.processor.model.BattleInfo;
import ru.otus.courses.kafka.battle.results.processor.model.PlayerBattleResult;

@UtilityClass
public class MappingUtils {

  public static PlayerBattleTotalResult toAvroRecord(long playerId, BattleInfo battleInfo,
                                                     PlayerBattleResult model) {
    return PlayerBattleTotalResult.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setMap(battleInfo.getMap())
        .setBattleId(battleInfo.getBattleId())
        .setBattleFinishedTimestamp(battleInfo.getBattleFinishedServerTime())
        .setPlayerId(playerId)
        .setDamageSum(model.getDamageSum())
        .setShots(model.getShotsCount())
        .setSuccessfulShots(model.getSuccessfulShotsCount())
        .setHeadshots(model.getHeadshotsCount())
        .setKilled(model.getKillsCount())
        .setDeaths(model.getDeathsCount())
        .setWinner(battleInfo.getWinners().contains(playerId))
        .setWeaponResults(model.getResultsByWeapon().entrySet().stream()
            .map(weaponResult -> WeaponResult.newBuilder()
                .setWeaponId(weaponResult.getKey())
                .setDamage(weaponResult.getValue().getDamageSum())
                .setHeadshots(weaponResult.getValue().getHeadshotsCount())
                .setKilled(weaponResult.getValue().getKillsCount())
                .setShots(weaponResult.getValue().getShotsCount())
                .setSuccessfulShots(weaponResult.getValue().getSuccessfulShotsCount())
                .build())
            .toList())
        .build();
  }
}
