package ru.otus.courses.kafka.battle.results.processor.serde;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import ru.otus.courses.kafka.battle.results.processor.model.BattleInfo;
import ru.otus.courses.kafka.battle.results.processor.model.PlayerBattleResult;
import ru.otus.courses.kafka.battle.results.processor.model.PlayerBattleResultsKey;

public class AppSerdes {

  private static <T> Serde<T> serde(Class<T> aClass) {
    return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(aClass));
  }

  public static Serde<PlayerBattleResultsKey> playerBattleResultsKey() {
    return serde(PlayerBattleResultsKey.class);
  }

  public static Serde<PlayerBattleResult> playerBattleResults() {
    return serde(PlayerBattleResult.class);
  }

  public static Serde<BattleInfo> battleTimes() {
    return serde(BattleInfo.class);
  }
}
