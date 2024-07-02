package ru.otus.courses.kafka.player.stats.processor.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

  @UtilityClass
  public static class Topics {

    public static final String PLAYER_BATTLE_RESULTS = "player-battle-results";
    public static final String PLAYER_STATS = "player-stats";
    public static final String PLAYER_STATS_DAILY = "player-stats-daily";

    public static final String PLAYER_COMMON_STATS_DB = "db.player-common-stats";
    public static final String PLAYER_MAP_STATS_DB = "db.player-map-stats";
    public static final String PLAYER_WEAPON_STATS_DB = "db.player-weapon-stats";
  }
}
