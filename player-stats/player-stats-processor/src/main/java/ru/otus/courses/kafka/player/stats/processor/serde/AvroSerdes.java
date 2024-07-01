package ru.otus.courses.kafka.player.stats.processor.serde;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import java.util.Map;
import org.apache.kafka.common.serialization.Serde;
import ru.otus.courses.kafka.battle.results.datatypes.PlayerBattleTotalResult;
import ru.otus.courses.kafka.player.stats.datatypes.PlayerStats;
import ru.otus.courses.kafka.player.stats.datatypes.PlayerStatsDailyKey;
import ru.otus.courses.kafka.player.stats.datatypes.db.PlayerCommonStatsRecord;
import ru.otus.courses.kafka.player.stats.datatypes.db.PlayerCommonStatsRecordKey;
import ru.otus.courses.kafka.player.stats.datatypes.db.PlayerMapStatsRecord;
import ru.otus.courses.kafka.player.stats.datatypes.db.PlayerMapStatsRecordKey;
import ru.otus.courses.kafka.player.stats.datatypes.db.PlayerWeaponStatsRecord;
import ru.otus.courses.kafka.player.stats.datatypes.db.PlayerWeaponStatsRecordKey;

public class AvroSerdes {

  public AvroSerdes(Map<String, Object> serdeConfig) {
    this.serdeConfig = serdeConfig;

    playerBattleTotalResultSerde = new SpecificAvroSerde<>();
    playerBattleTotalResultSerde.configure(serdeConfig, false);

    playerStatsSerde = new SpecificAvroSerde<>();
    playerStatsSerde.configure(serdeConfig, false);

    playerStatsDailyKeySerde = new SpecificAvroSerde<>();
    playerStatsDailyKeySerde.configure(serdeConfig, true);

    playerCommonStatsRecordSerde = new SpecificAvroSerde<>();
    playerCommonStatsRecordSerde.configure(serdeConfig, false);

    playerCommonStatsRecordKeySerde = new SpecificAvroSerde<>();
    playerCommonStatsRecordKeySerde.configure(serdeConfig, true);

    playerMapStatsRecordSerde = new SpecificAvroSerde<>();
    playerMapStatsRecordSerde.configure(serdeConfig, false);

    playerMapStatsRecordKeySerde = new SpecificAvroSerde<>();
    playerMapStatsRecordKeySerde.configure(serdeConfig, true);

    playerWeaponStatsRecordSerde = new SpecificAvroSerde<>();
    playerWeaponStatsRecordSerde.configure(serdeConfig, false);

    playerWeaponStatsRecordKeySerde = new SpecificAvroSerde<>();
    playerWeaponStatsRecordKeySerde.configure(serdeConfig, true);
  }

  private final Map<String, Object> serdeConfig;

  private final Serde<PlayerBattleTotalResult> playerBattleTotalResultSerde;

  private final Serde<PlayerStats> playerStatsSerde;

  private final Serde<PlayerStatsDailyKey> playerStatsDailyKeySerde;

  private final Serde<PlayerCommonStatsRecord> playerCommonStatsRecordSerde;

  private final Serde<PlayerCommonStatsRecordKey> playerCommonStatsRecordKeySerde;

  private final Serde<PlayerMapStatsRecord> playerMapStatsRecordSerde;

  private final Serde<PlayerMapStatsRecordKey> playerMapStatsRecordKeySerde;

  private final Serde<PlayerWeaponStatsRecord> playerWeaponStatsRecordSerde;

  private final Serde<PlayerWeaponStatsRecordKey> playerWeaponStatsRecordKeySerde;

  public Serde<PlayerBattleTotalResult> playerBattleTotalResult() {
    return playerBattleTotalResultSerde;
  }

  public Serde<PlayerStats> playerStats() {
    return playerStatsSerde;
  }

  public Serde<PlayerStatsDailyKey> playerStatsDailyKey() {
    return playerStatsDailyKeySerde;
  }

  public Serde<PlayerCommonStatsRecord> playerCommonStatsRecord() {
    return playerCommonStatsRecordSerde;
  }

  public Serde<PlayerCommonStatsRecordKey> playerCommonStatsRecordKey() {
    return playerCommonStatsRecordKeySerde;
  }

  public Serde<PlayerMapStatsRecord> playerMapStatsRecord() {
    return playerMapStatsRecordSerde;
  }

  public Serde<PlayerMapStatsRecordKey> playerMapStatsRecordKey() {
    return playerMapStatsRecordKeySerde;
  }

  public Serde<PlayerWeaponStatsRecord> playerWeaponStatsRecord() {
    return playerWeaponStatsRecordSerde;
  }

  public Serde<PlayerWeaponStatsRecordKey> playerWeaponStatsRecordKey() {
    return playerWeaponStatsRecordKeySerde;
  }
}
