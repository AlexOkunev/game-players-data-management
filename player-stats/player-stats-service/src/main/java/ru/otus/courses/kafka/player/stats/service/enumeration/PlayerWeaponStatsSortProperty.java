package ru.otus.courses.kafka.player.stats.service.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.courses.kafka.player.stats.service.entity.PlayerWeaponStats.Fields;

@Getter
@AllArgsConstructor
public enum PlayerWeaponStatsSortProperty {
  BATTLES_COUNT(Fields.battlesCount),
  SHOTS_COUNT(Fields.shotsCount),
  SUCCESSFUL_SHOTS_COUNT(Fields.successfulShotsCount),
  HEADSHOTS_COUNT(Fields.headshotsCount),
  KILLS_COUNT(Fields.killsCount),
  DAMAGE_SUM(Fields.damageSum),
  SUCCESSFUL_SHOTS_RATE(Fields.successfulShotsRate),
  HEADSHOTS_TO_SUCCESSFUL_SHOTS_RATE(Fields.headshotsToSuccessfulShotsRate),
  AVG_SUCCESSFUL_SHOT_DAMAGE(Fields.avgSuccessfulShotDamage);

  private final String fieldName;
}
