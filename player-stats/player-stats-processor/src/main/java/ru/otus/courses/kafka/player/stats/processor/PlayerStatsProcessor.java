package ru.otus.courses.kafka.player.stats.processor;

import static ru.otus.courses.kafka.player.stats.processor.config.Configuration.SERDE_CONFIG;
import static ru.otus.courses.kafka.player.stats.processor.config.Configuration.STREAMS_CONFIG;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import ru.otus.courses.kafka.player.stats.processor.aggregators.PlayerStatsAggregator;
import ru.otus.courses.kafka.player.stats.processor.config.Constants.Topics;
import ru.otus.courses.kafka.player.stats.processor.mapping.Mappers;
import ru.otus.courses.kafka.player.stats.processor.model.PlayerStatsAggregateResult;
import ru.otus.courses.kafka.player.stats.processor.serde.AppSerdes;
import ru.otus.courses.kafka.player.stats.processor.serde.AvroSerdes;
import ru.otus.courses.kafka.player.stats.processor.time.BattleFinishedTimestampExtractor;

@Slf4j
public class PlayerStatsProcessor {

  public static void main(String[] args) {
    AvroSerdes avroSerdes = new AvroSerdes(SERDE_CONFIG);

    var builder = new StreamsBuilder();

    var groupedStream = builder
        .stream(Topics.PLAYER_BATTLE_RESULTS,
            Consumed.with(Serdes.String(), avroSerdes.playerBattleTotalResult())
                .withTimestampExtractor(new BattleFinishedTimestampExtractor()))
        .peek((k, v) -> log.info("Processing results {} for player {}. Key {}", v.getEventId(), v.getPlayerId(), k))
        .groupByKey();

    var aggregateResultStream = groupedStream
        .aggregate(PlayerStatsAggregateResult::new, new PlayerStatsAggregator(),
            Materialized.with(Serdes.String(), AppSerdes.playerStatsAggregateResult()))
        .toStream();

    aggregateResultStream
        .mapValues(Mappers.playerStatsValueMapper())
        .peek((k, v) -> log.info("Send stats {} for player {}. Key {}", v.getEventId(),
            v.getPlayerId(), k))
        .to(Topics.PLAYER_STATS, Produced.with(Serdes.String(), avroSerdes.playerStats()));

    aggregateResultStream
        .map(Mappers.playerCommonStatsRecordKeyValueMapper())
        .peek((k, v) -> log.info("Send stats {} to db topic. Key {}", v.getEventId(), k.getPlayerId()))
        .to(Topics.PLAYER_COMMON_STATS_DB,
            Produced.with(avroSerdes.playerCommonStatsRecordKey(), avroSerdes.playerCommonStatsRecord()));

    aggregateResultStream
        .flatMap(Mappers.playerMapStatsRecordKeyValueMapper())
        .peek((k, v) -> log.info("Send stats {} to db topic. Key ({},{})", v.getEventId(), k.getPlayerId(), k.getMap()))
        .to(Topics.PLAYER_MAP_STATS_DB,
            Produced.with(avroSerdes.playerMapStatsRecordKey(), avroSerdes.playerMapStatsRecord()));

    aggregateResultStream
        .flatMap(Mappers.playerWeaponStatsRecordKeyValueMapper())
        .peek((k, v) ->
            log.info("Send stats {} to db topic. Key ({},{})", v.getEventId(), k.getPlayerId(), k.getWeaponId()))
        .to(Topics.PLAYER_WEAPON_STATS_DB,
            Produced.with(avroSerdes.playerWeaponStatsRecordKey(), avroSerdes.playerWeaponStatsRecord()));

    groupedStream
        .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofDays(1)))
        .aggregate(PlayerStatsAggregateResult::new, new PlayerStatsAggregator(),
            Materialized.with(Serdes.String(), AppSerdes.playerStatsAggregateResult()))
        .toStream()
        .map(Mappers.playerStatsDailyKeyValueMapper())
        .peek((key, value) -> log.info("Send daily stats {} for player {}. Key ({},{})", value.getEventId(),
            value.getPlayerId(), key.getDate(), key.getPlayerId()))
        .to(Topics.PLAYER_STATS_DAILY, Produced.with(avroSerdes.playerStatsDailyKey(), avroSerdes.playerStats()));

    KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), new StreamsConfig(STREAMS_CONFIG));
    kafkaStreams.start();

    log.info("App Started");

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      kafkaStreams.close();
    }));
  }

}