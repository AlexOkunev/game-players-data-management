package ru.otus.courses.kafka.battle.results.processor.serde;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import org.apache.kafka.common.serialization.Serializer;
import ru.otus.courses.kafka.battle.results.processor.util.InstantGsonAdapter;

public class JsonSerializer<T> implements Serializer<T> {

  private final Gson gson = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .registerTypeAdapter(Instant.class, new InstantGsonAdapter())
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