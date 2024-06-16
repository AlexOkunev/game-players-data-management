
package ru.otus.courses.kafka;

import static java.util.Collections.singletonMap;
import static java.util.UUID.randomUUID;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import ru.otus.courses.kafka.game.server.datatypes.MatchStarted;

@Slf4j
public class Main {

  public static void main(String[] args) throws InterruptedException {
    StreamsBuilder builder = new StreamsBuilder();

    //TODO maybe not random but constant app ID
    final Map<String, Object> STREAMS_CONFIG = Map.of(
        StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091,localhost:9092,localhost:9093",
        StreamsConfig.APPLICATION_ID_CONFIG, "match-results-%s".formatted(randomUUID().hashCode()),
        StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 500);

    final Map<String, String> serdeConfig = singletonMap("schema.registry.url", "http://localhost:8081");

    final Serde<MatchStarted> valueSpecificAvroSerde = new SpecificAvroSerde<>();
    valueSpecificAvroSerde.configure(serdeConfig, false); // `false` for record values

    builder.stream("topic1", Consumed.with(Serdes.String(), valueSpecificAvroSerde))
        .foreach(
            (k, v) -> log.info("{}: {}, {}, {}", k,
                Instant.ofEpochMilli(v.getTimestamp()).atOffset(ZoneOffset.ofHours(5)).toLocalDateTime().toString(),
                v.getId(), v.getMap().name()));

    try (var kafkaStreams = new KafkaStreams(builder.build(), new StreamsConfig(STREAMS_CONFIG))) {
      log.info("App Started");

      kafkaStreams.start();

      while (!Thread.interrupted()) {
        Thread.sleep(1000);
      }

      log.info("Shutting down now");
    }
  }
}