
package ru.otus.courses.kafka.battle.results.service;

import static ru.otus.courses.kafka.battle.results.service.config.Constants.StateStores.BATTLES_TO_SEND_STATE_STORE;
import static ru.otus.courses.kafka.battle.results.service.config.Constants.StateStores.BATTLE_INFO_STATE_STORE;
import static ru.otus.courses.kafka.battle.results.service.config.Constants.StateStores.BATTLE_RESULTS_STATE_STORE;
import static ru.otus.courses.kafka.battle.results.service.config.Constants.Topics.BATTLE_EVENTS;
import static ru.otus.courses.kafka.battle.results.service.config.Constants.Topics.PLAYER_BATTLE_RESULTS;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.apache.kafka.streams.state.Stores;
import ru.otus.courses.kafka.battle.results.datatypes.PlayerBattleTotalResult;
import ru.otus.courses.kafka.battle.results.service.processors.BattleEventsProcessor;
import ru.otus.courses.kafka.battle.results.service.processors.BattleInfoProcessor;
import ru.otus.courses.kafka.battle.results.service.processors.BattleTimeProcessor;
import ru.otus.courses.kafka.battle.results.service.serde.AppSerdes;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleEvent;

@Slf4j
public class Application {

  public static void main(String[] args) {
    final Map<String, Object> STREAMS_CONFIG = Map.of(
        StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091,localhost:9092,localhost:9093",
        StreamsConfig.APPLICATION_ID_CONFIG, "battle-results",
        StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class,
        StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 500,
        StreamsConfig.STATE_DIR_CONFIG, "/tmp/battle-results-service");

    final Map<String, Object> serdeConfig = Map.of(
        KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

    Serde<BattleEvent> battleEventAvroSerde = new SpecificAvroSerde<>();
    battleEventAvroSerde.configure(serdeConfig, false);

    Serde<PlayerBattleTotalResult> playerBattleTotalResultAvroSerde = new SpecificAvroSerde<>();
    playerBattleTotalResultAvroSerde.configure(serdeConfig, false);

    var builder = new StreamsBuilder();

    builder.addStateStore(Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(BATTLE_RESULTS_STATE_STORE),
        AppSerdes.playerBattleResultsKey(), AppSerdes.playerBattleResults()));

    builder.addStateStore(Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(BATTLE_INFO_STATE_STORE),
        Serdes.String(), AppSerdes.battleTimes()));

    builder.addStateStore(Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(BATTLES_TO_SEND_STATE_STORE),
        Serdes.String(), Serdes.Long()));

    builder
        .stream(BATTLE_EVENTS, Consumed.with(Serdes.String(), battleEventAvroSerde))
        .process(BattleTimeProcessor::new, BATTLE_INFO_STATE_STORE)
        .process(BattleInfoProcessor::new, BATTLE_INFO_STATE_STORE)
        .process(BattleEventsProcessor::new, BATTLE_RESULTS_STATE_STORE, BATTLE_INFO_STATE_STORE,
            BATTLES_TO_SEND_STATE_STORE)
        .to(PLAYER_BATTLE_RESULTS, Produced.with(Serdes.String(), playerBattleTotalResultAvroSerde));

    KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), new StreamsConfig(STREAMS_CONFIG));
    kafkaStreams.start();

    log.info("App Started");

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      log.info("Shut down app;");
      kafkaStreams.close();
    }));
  }

}