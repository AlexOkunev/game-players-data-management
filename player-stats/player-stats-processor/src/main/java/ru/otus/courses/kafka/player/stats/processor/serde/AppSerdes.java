package ru.otus.courses.kafka.player.stats.processor.serde;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import ru.otus.courses.kafka.player.stats.processor.model.PlayerStatsAggregateResult;

public class AppSerdes {

  private static <T> Serde<T> serde(Class<T> aClass) {
    return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(aClass));
  }

  public static Serde<PlayerStatsAggregateResult> playerStatsAggregateResult() {
    return serde(PlayerStatsAggregateResult.class);
  }
}
