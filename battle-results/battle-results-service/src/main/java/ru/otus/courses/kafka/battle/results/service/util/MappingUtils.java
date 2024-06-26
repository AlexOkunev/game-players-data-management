package ru.otus.courses.kafka.battle.results.service.util;

import lombok.experimental.UtilityClass;
import ru.otus.courses.kafka.battle.results.datatypes.PlayerBattleTotalResult;
import ru.otus.courses.kafka.battle.results.datatypes.WeaponResult;
import ru.otus.courses.kafka.battle.results.service.model.BattleInfo;
import ru.otus.courses.kafka.battle.results.service.model.PlayerBattleResult;

@UtilityClass
public class MappingUtils {

  public static PlayerBattleTotalResult toAvroRecord(long playerId, BattleInfo battleInfo,
                                                     PlayerBattleResult model) {
    return PlayerBattleTotalResult.newBuilder()
        .setMap(battleInfo.getMap())
        .setBattleId(battleInfo.getBattleId())
        .setPlayerId(playerId)
        .setDamageSum(model.getDamageSum())
        .setShots(model.getShotsCount())
        .setSuccessfulShots(model.getSuccessfulShotsCount())
        .setHeadshots(model.getHeadshotsCount())
        .setKilled(model.getKillsCount())
        .setDeaths(model.getDeathsCount())
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
