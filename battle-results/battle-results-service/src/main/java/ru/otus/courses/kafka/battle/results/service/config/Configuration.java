package ru.otus.courses.kafka.battle.results.service.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import java.util.Map;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;

public class Configuration {

  public static final Map<String, Object> STREAMS_CONFIG = Map.of(
      StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091,localhost:9092,localhost:9093",
      StreamsConfig.APPLICATION_ID_CONFIG, "battle-results",
      StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class,
      StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 500,
      StreamsConfig.STATE_DIR_CONFIG, "/tmp/battle-results-service");

  public static final Map<String, Object> SERDE_CONFIG = Map.of(
      KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
}
