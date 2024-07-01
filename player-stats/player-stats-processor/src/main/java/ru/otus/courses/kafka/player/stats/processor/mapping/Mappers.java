package ru.otus.courses.kafka.player.stats.processor.mapping;

import lombok.experimental.UtilityClass;
import ru.otus.courses.kafka.player.stats.processor.mapping.db.PlayerCommonStatsRecordKeyValueMapper;
import ru.otus.courses.kafka.player.stats.processor.mapping.db.PlayerCommonStatsRecordValueMapper;
import ru.otus.courses.kafka.player.stats.processor.mapping.db.PlayerMapStatsRecordKeyValueMapper;
import ru.otus.courses.kafka.player.stats.processor.mapping.db.PlayerMapStatsRecordValueMapper;
import ru.otus.courses.kafka.player.stats.processor.mapping.db.PlayerWeaponStatsRecordKeyValueMapper;
import ru.otus.courses.kafka.player.stats.processor.mapping.db.PlayerWeaponStatsRecordValueMapper;

@UtilityClass
public class Mappers {

  private static final PlayerStatsValueMapper PLAYER_STATS_VALUE_MAPPER = new PlayerStatsValueMapper();
  private static final PlayerStatsDailyKeyValueMapper PLAYER_STATS_DAILY_KEY_VALUE_MAPPER = new PlayerStatsDailyKeyValueMapper(
      PLAYER_STATS_VALUE_MAPPER);

  private static final PlayerCommonStatsRecordKeyValueMapper PLAYER_COMMON_STATS_RECORD_KEY_VALUE_MAPPER = new PlayerCommonStatsRecordKeyValueMapper(
      new PlayerCommonStatsRecordValueMapper());

  private static final PlayerWeaponStatsRecordKeyValueMapper PLAYER_WEAPON_STATS_RECORD_KEY_VALUE_MAPPER = new PlayerWeaponStatsRecordKeyValueMapper(
      new PlayerWeaponStatsRecordValueMapper());

  private static final PlayerMapStatsRecordKeyValueMapper PLAYER_MAP_STATS_RECORD_KEY_VALUE_MAPPER = new PlayerMapStatsRecordKeyValueMapper(
      new PlayerMapStatsRecordValueMapper());

  public static PlayerStatsValueMapper playerStatsValueMapper() {
    return PLAYER_STATS_VALUE_MAPPER;
  }

  public static PlayerStatsDailyKeyValueMapper playerStatsDailyKeyValueMapper() {
    return PLAYER_STATS_DAILY_KEY_VALUE_MAPPER;
  }

  public static PlayerCommonStatsRecordKeyValueMapper playerCommonStatsRecordKeyValueMapper() {
    return PLAYER_COMMON_STATS_RECORD_KEY_VALUE_MAPPER;
  }

  public static PlayerWeaponStatsRecordKeyValueMapper playerWeaponStatsRecordKeyValueMapper() {
    return PLAYER_WEAPON_STATS_RECORD_KEY_VALUE_MAPPER;
  }

  public static PlayerMapStatsRecordKeyValueMapper playerMapStatsRecordKeyValueMapper() {
    return PLAYER_MAP_STATS_RECORD_KEY_VALUE_MAPPER;
  }
}
