package ru.otus.courses.kafka.player.stats.service.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortProperty {
  BATTLES_COUNT("battlesCount"),
  SHOTS_COUNT("shotsCount"),
  SUCCESSFUL_SHOTS_COUNT("successfulShotsCount"),
  HEADSHOTS_COUNT("headshotsCount"),
  KILLS_COUNT("killsCount"),
  DEATHS_COUNT("deathsCount"),
  WINS_COUNT("winsCount"),
  DAMAGE_SUM("damageSum"),
  SUCCESSFUL_SHOTS_RATE("successfulShotsRate"),
  HEADSHOTS_TO_SUCCESSFUL_SHOTS_RATE("headshotsToSuccessfulShotsRate"),
  AVG_SUCCESSFUL_SHOT_DAMAGE("avgSuccessfulShotDamage"),
  WINS_RATE("winsRate");

  private final String fieldName;

}
