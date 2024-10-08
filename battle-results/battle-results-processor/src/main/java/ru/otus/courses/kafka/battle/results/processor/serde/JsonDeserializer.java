package ru.otus.courses.kafka.battle.results.processor.serde;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import ru.otus.courses.kafka.battle.results.processor.util.InstantGsonAdapter;

public class JsonDeserializer<T> implements Deserializer<T> {

  private final Gson gson =
      new GsonBuilder()
          .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .registerTypeAdapter(Instant.class, new InstantGsonAdapter())
          .create();

  private Class<T> destinationClass;

  private Type reflectionTypeToken;

  /**
   * Default constructor needed by Kafka
   */
  public JsonDeserializer(Class<T> destinationClass) {
    this.destinationClass = destinationClass;
  }

  public JsonDeserializer(Type reflectionTypeToken) {
    this.reflectionTypeToken = reflectionTypeToken;
  }

  @Override
  public void configure(Map<String, ?> props, boolean isKey) {
  }

  @Override
  public T deserialize(String topic, byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    Type type = destinationClass != null ? destinationClass : reflectionTypeToken;
    return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), type);
  }

  @Override
  public void close() {
  }
}
