package ru.otus.courses.kafka.battle.results.service.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class PlayerBattleResult {

  private final Map<Long, PlayerBattleWeaponResult> resultsByWeapon = new HashMap<>();

  private int deathsCount;

  public PlayerBattleWeaponResult getWeaponResult(long weaponId) {
    resultsByWeapon.putIfAbsent(weaponId, new PlayerBattleWeaponResult());
    return resultsByWeapon.get(weaponId);
  }

  public void incDeathsCount() {
    deathsCount++;
  }

  public int getShotsCount() {
    return resultsByWeapon.values().stream()
        .mapToInt(PlayerBattleWeaponResult::getShotsCount)
        .sum();
  }

  public int getSuccessfulShotsCount() {
    return resultsByWeapon.values().stream()
        .mapToInt(PlayerBattleWeaponResult::getSuccessfulShotsCount)
        .sum();
  }

  public int getDamageSum() {
    return resultsByWeapon.values().stream()
        .mapToInt(PlayerBattleWeaponResult::getDamageSum)
        .sum();
  }

  public int getKillsCount() {
    return resultsByWeapon.values().stream()
        .mapToInt(PlayerBattleWeaponResult::getKillsCount)
        .sum();
  }

  public int getHeadshotsCount() {
    return resultsByWeapon.values().stream()
        .mapToInt(PlayerBattleWeaponResult::getHeadshotsCount)
        .sum();
  }
}
