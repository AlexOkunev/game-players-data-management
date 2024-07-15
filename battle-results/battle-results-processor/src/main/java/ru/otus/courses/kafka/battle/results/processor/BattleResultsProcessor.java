package ru.otus.courses.kafka.battle.results.processor;

import static ru.otus.courses.kafka.battle.results.processor.config.Configuration.SERDE_CONFIG;
import static ru.otus.courses.kafka.battle.results.processor.config.Configuration.STREAMS_CONFIG;
import static ru.otus.courses.kafka.battle.results.processor.config.Constants.StateStores.BATTLES_TO_SEND_STATE_STORE;
import static ru.otus.courses.kafka.battle.results.processor.config.Constants.StateStores.BATTLE_INFO_STATE_STORE;
import static ru.otus.courses.kafka.battle.results.processor.config.Constants.StateStores.BATTLE_RESULTS_STATE_STORE;
import static ru.otus.courses.kafka.battle.results.processor.config.Constants.Topics.BATTLE_EVENTS;
import static ru.otus.courses.kafka.battle.results.processor.config.Constants.Topics.PLAYER_BATTLE_RESULTS;
import static ru.otus.courses.kafka.battle.results.processor.config.Constants.Topics.PLAYER_BATTLE_RESULTS_DB;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.Stores;
import ru.otus.courses.kafka.battle.results.processor.processors.BattleInfoProcessor;
import ru.otus.courses.kafka.battle.results.processor.processors.BattleShotsProcessor;
import ru.otus.courses.kafka.battle.results.processor.processors.BattleTimeProcessor;
import ru.otus.courses.kafka.battle.results.processor.serde.AppSerdes;
import ru.otus.courses.kafka.battle.results.processor.serde.AvroSerdes;
import ru.otus.courses.kafka.battle.results.processor.util.DbMappingUtils;

@Slf4j
public class BattleResultsProcessor {

  public static void main(String[] args) {
    AvroSerdes avroSerdes = new AvroSerdes(SERDE_CONFIG);

    var builder = new StreamsBuilder();

    builder.addStateStore(Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(BATTLE_RESULTS_STATE_STORE),
        AppSerdes.playerBattleResultsKey(), AppSerdes.playerBattleResults()));

    builder.addStateStore(Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(BATTLE_INFO_STATE_STORE),
        Serdes.String(), AppSerdes.battleTimes()));

    builder.addStateStore(Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(BATTLES_TO_SEND_STATE_STORE),
        Serdes.String(), Serdes.Long()));

    var resultsStream = builder
        .stream(BATTLE_EVENTS, Consumed.with(Serdes.String(), avroSerdes.battleEventSerde()))
        .process(BattleTimeProcessor::new, BATTLE_INFO_STATE_STORE)
        .process(BattleInfoProcessor::new, BATTLE_INFO_STATE_STORE)
        .process(BattleShotsProcessor::new, BATTLE_RESULTS_STATE_STORE, BATTLE_INFO_STATE_STORE,
            BATTLES_TO_SEND_STATE_STORE);

    resultsStream
        .to(PLAYER_BATTLE_RESULTS, Produced.with(Serdes.String(), avroSerdes.playerBattleTotalResult()));

    resultsStream
        .map((key, value) -> new KeyValue<>(DbMappingUtils.mapKey(value), DbMappingUtils.mapValue(value)))
        .to(PLAYER_BATTLE_RESULTS_DB,
            Produced.with(avroSerdes.playerBattleTotalResultRecordKeySerde(),
                avroSerdes.playerBattleTotalResultRecordSerde()));

    KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), new StreamsConfig(STREAMS_CONFIG));
    kafkaStreams.start();

    log.info("App Started");

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      log.info("Shut down app;");
      kafkaStreams.close();
    }));
  }

}