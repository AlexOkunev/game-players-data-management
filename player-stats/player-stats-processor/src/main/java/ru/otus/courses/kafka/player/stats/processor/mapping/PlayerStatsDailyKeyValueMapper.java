package ru.otus.courses.kafka.player.stats.processor.mapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.kstream.Windowed;
import ru.otus.courses.kafka.player.stats.datatypes.PlayerStats;
import ru.otus.courses.kafka.player.stats.datatypes.PlayerStatsDailyKey;
import ru.otus.courses.kafka.player.stats.processor.model.PlayerStatsAggregateResult;

@RequiredArgsConstructor
public class PlayerStatsDailyKeyValueMapper implements
    KeyValueMapper<Windowed<String>, PlayerStatsAggregateResult, KeyValue<PlayerStatsDailyKey, PlayerStats>> {

  private final ValueMapper<PlayerStatsAggregateResult, PlayerStats> playerStatsValueMapper;

  @Override
  public KeyValue<PlayerStatsDailyKey, PlayerStats> apply(Windowed<String> key, PlayerStatsAggregateResult value) {
    PlayerStatsDailyKey dailyKey = PlayerStatsDailyKey.newBuilder()
        .setPlayerId(value.getPlayerId())
        .setDate(LocalDate.ofInstant(Instant.ofEpochMilli(key.window().start()), ZoneId.systemDefault()))
        .build();

    PlayerStats playerStats = playerStatsValueMapper.apply(value);

    return new KeyValue<>(dailyKey, playerStats);
  }
}
