package ru.otus.courses.kafka.player.stats.processor.mapping.db;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.ValueMapper;
import ru.otus.courses.kafka.player.stats.datatypes.db.PlayerCommonStatsRecord;
import ru.otus.courses.kafka.player.stats.datatypes.db.PlayerCommonStatsRecordKey;
import ru.otus.courses.kafka.player.stats.processor.model.PlayerStatsAggregateResult;

@RequiredArgsConstructor
public class PlayerCommonStatsRecordKeyValueMapper implements
    KeyValueMapper<String, PlayerStatsAggregateResult, KeyValue<PlayerCommonStatsRecordKey, PlayerCommonStatsRecord>> {

  private final ValueMapper<PlayerStatsAggregateResult, PlayerCommonStatsRecord> valueMapper;

  @Override
  public KeyValue<PlayerCommonStatsRecordKey, PlayerCommonStatsRecord> apply(String key,
                                                                             PlayerStatsAggregateResult value) {
    return new KeyValue<>(
        PlayerCommonStatsRecordKey.newBuilder()
            .setPlayerId(value.getPlayerId())
            .build(),
        valueMapper.apply(value));
  }
}
