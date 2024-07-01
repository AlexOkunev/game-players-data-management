package ru.otus.courses.kafka.battle.results.service;

import static ru.otus.courses.kafka.battle.results.service.config.Configuration.SERDE_CONFIG;
import static ru.otus.courses.kafka.battle.results.service.config.Configuration.STREAMS_CONFIG;
import static ru.otus.courses.kafka.battle.results.service.config.Constants.StateStores.BATTLES_TO_SEND_STATE_STORE;
import static ru.otus.courses.kafka.battle.results.service.config.Constants.StateStores.BATTLE_INFO_STATE_STORE;
import static ru.otus.courses.kafka.battle.results.service.config.Constants.StateStores.BATTLE_RESULTS_STATE_STORE;
import static ru.otus.courses.kafka.battle.results.service.config.Constants.Topics.BATTLE_EVENTS;
import static ru.otus.courses.kafka.battle.results.service.config.Constants.Topics.PLAYER_BATTLE_RESULTS;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.Stores;
import ru.otus.courses.kafka.battle.results.datatypes.PlayerBattleTotalResult;
import ru.otus.courses.kafka.battle.results.service.processors.BattleInfoProcessor;
import ru.otus.courses.kafka.battle.results.service.processors.BattleShotsProcessor;
import ru.otus.courses.kafka.battle.results.service.processors.BattleTimeProcessor;
import ru.otus.courses.kafka.battle.results.service.serde.AppSerdes;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleEvent;

@Slf4j
public class BattleResultsService {

  public static void main(String[] args) {
    Serde<BattleEvent> battleEventAvroSerde = new SpecificAvroSerde<>();
    battleEventAvroSerde.configure(SERDE_CONFIG, false);

    Serde<PlayerBattleTotalResult> playerBattleTotalResultAvroSerde = new SpecificAvroSerde<>();
    playerBattleTotalResultAvroSerde.configure(SERDE_CONFIG, false);

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
        .process(BattleShotsProcessor::new, BATTLE_RESULTS_STATE_STORE, BATTLE_INFO_STATE_STORE,
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