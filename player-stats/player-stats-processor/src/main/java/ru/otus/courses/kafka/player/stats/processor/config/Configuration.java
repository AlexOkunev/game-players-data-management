package ru.otus.courses.kafka.player.stats.processor.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import java.util.Map;
import org.apache.kafka.streams.StreamsConfig;

public class Configuration {

  public static final Map<String, Object> STREAMS_CONFIG = Map.of(
      StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091,localhost:9092,localhost:9093",
      StreamsConfig.APPLICATION_ID_CONFIG, "player-stats",
      StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 500,
      StreamsConfig.STATE_DIR_CONFIG, "/tmp/player-stats-service");

  public static final Map<String, Object> SERDE_CONFIG = Map.of(
      KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
}
