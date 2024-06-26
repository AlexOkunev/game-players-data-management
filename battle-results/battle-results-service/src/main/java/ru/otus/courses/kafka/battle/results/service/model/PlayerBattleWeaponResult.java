package ru.otus.courses.kafka.battle.results.service.model;

import lombok.Data;

@Data
public class PlayerBattleWeaponResult {

  private int shotsCount = 0;

  private int successfulShotsCount = 0;

  private int damageSum = 0;

  private int headshotsCount = 0;

  private int killsCount = 0;

  public void addShot(Long victimPlayerId, int damage) {
    damageSum += damage;
    shotsCount++;

    if (damage > 0 && victimPlayerId != null) {
      successfulShotsCount++;
    }
  }

  public void incHeadshotsCount() {
    headshotsCount++;
  }

  public void incKillsCount() {
    killsCount++;
  }

}
