package ru.otus.courses.kafka.player.stats.processor.serde;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerializer<T> implements Serializer<T> {

  private final Gson gson = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create();

  @Override
  public void configure(Map<String, ?> props, boolean isKey) {
  }

  @Override
  public byte[] serialize(String topic, T type) {
    return gson.toJson(type).getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public void close() {
  }
}