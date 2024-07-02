package ru.otus.courses.kafka.battle.results.service.serde;

import static ru.otus.courses.kafka.battle.results.service.config.Configuration.SERDE_CONFIG;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import java.util.Map;
import org.apache.kafka.common.serialization.Serde;
import ru.otus.courses.kafka.battle.results.datatypes.PlayerBattleTotalResult;
import ru.otus.courses.kafka.battle.results.datatypes.db.PlayerBattleTotalResultRecord;
import ru.otus.courses.kafka.battle.results.datatypes.db.PlayerBattleTotalResultRecordKey;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleEvent;

public class AvroSerdes {

  public AvroSerdes(Map<String, Object> serdeConfig) {
    this.serdeConfig = serdeConfig;

    playerBattleTotalResultSerde = new SpecificAvroSerde<>();
    playerBattleTotalResultSerde.configure(serdeConfig, false);

    battleEventSerde = new SpecificAvroSerde<>();
    battleEventSerde.configure(SERDE_CONFIG, false);

    playerBattleTotalResultRecordSerde = new SpecificAvroSerde<>();
    playerBattleTotalResultRecordSerde.configure(serdeConfig, false);

    playerBattleTotalResultRecordKeySerde = new SpecificAvroSerde<>();
    playerBattleTotalResultRecordKeySerde.configure(serdeConfig, true);
  }

  private final Map<String, Object> serdeConfig;

  private final Serde<PlayerBattleTotalResult> playerBattleTotalResultSerde;

  private final Serde<PlayerBattleTotalResultRecord> playerBattleTotalResultRecordSerde;

  private final Serde<PlayerBattleTotalResultRecordKey> playerBattleTotalResultRecordKeySerde;

  private final Serde<BattleEvent> battleEventSerde;

  public Serde<PlayerBattleTotalResult> playerBattleTotalResult() {
    return playerBattleTotalResultSerde;
  }

  public Serde<PlayerBattleTotalResultRecord> playerBattleTotalResultRecordSerde() {
    return playerBattleTotalResultRecordSerde;
  }

  public Serde<PlayerBattleTotalResultRecordKey> playerBattleTotalResultRecordKeySerde() {
    return playerBattleTotalResultRecordKeySerde;
  }

  public Serde<BattleEvent> battleEventSerde() {
    return battleEventSerde;
  }


}
