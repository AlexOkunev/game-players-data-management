package ru.otus.courses.kafka.player.stats.processor.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PlayerStatsAggregateResult {

  @Setter
  private Long playerId;

  private int battlesCount = 0;

  private int shotsCount = 0;

  private int successfulShotsCount = 0;

  private int headshotsCount = 0;

  private int killsCount = 0;

  private int deathsCount = 0;

  private int winsCount = 0;

  private int damageSum = 0;

  private final Map<String, MapStatsAggregateResult> statsByMap = new HashMap<>();

  private final Map<Long, WeaponStatsAggregateResult> statsByWeapon = new HashMap<>();

  public MapStatsAggregateResult getStatsByMap(String mapName) {
    statsByMap.putIfAbsent(mapName, new MapStatsAggregateResult());
    return statsByMap.get(mapName);
  }

  public WeaponStatsAggregateResult getStatsByWeaponId(Long weaponId) {
    statsByWeapon.putIfAbsent(weaponId, new WeaponStatsAggregateResult());
    return statsByWeapon.get(weaponId);
  }

  public float getSuccessfulShotsRate() {
    return shotsCount > 0 ? (float) successfulShotsCount / shotsCount : 0;
  }

  public float getHeadshotsToSuccessfulShotsRate() {
    return successfulShotsCount > 0 ? (float) headshotsCount / successfulShotsCount : 0;
  }

  public float getAvgSuccessfulShotDamage() {
    return successfulShotsCount > 0 ? (float) damageSum / successfulShotsCount : 0;
  }

  public float getWinsRate() {
    return battlesCount > 0 ? (float) winsCount / battlesCount : 0;
  }

  public void incBattlesCount() {
    battlesCount++;
  }

  public void incWinsCount() {
    winsCount++;
  }

  public void addShotsCount(int shotsCount) {
    this.shotsCount += shotsCount;
  }

  public void addSuccessfulShotsCount(int successfulShotsCount) {
    this.successfulShotsCount += successfulShotsCount;
  }

  public void addHeadshotsCount(int headshotsCount) {
    this.headshotsCount += headshotsCount;
  }

  public void addKillsCount(int killsCount) {
    this.killsCount += killsCount;
  }

  public void addDeathsCount(int deathsCount) {
    this.deathsCount += deathsCount;
  }

  public void addDamage(int damage) {
    damageSum += damage;
  }
}
