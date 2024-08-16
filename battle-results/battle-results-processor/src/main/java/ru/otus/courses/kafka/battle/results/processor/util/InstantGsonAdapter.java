package ru.otus.courses.kafka.battle.results.processor.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.Instant;

public class InstantGsonAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

  @Override
  public JsonElement serialize(Instant instant, Type type, JsonSerializationContext context) {

    return new JsonPrimitive(instant.toEpochMilli());

  }

  @Override
  public Instant deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
      throws JsonParseException {

    return Instant.ofEpochMilli(jsonElement.getAsJsonPrimitive().getAsLong());
  }

}
