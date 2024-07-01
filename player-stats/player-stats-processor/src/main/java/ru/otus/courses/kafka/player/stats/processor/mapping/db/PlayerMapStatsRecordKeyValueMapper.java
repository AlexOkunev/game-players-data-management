package ru.otus.courses.kafka.player.stats.processor.mapping.db;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.ValueMapper;
import ru.otus.courses.kafka.player.stats.datatypes.db.PlayerMapStatsRecord;
import ru.otus.courses.kafka.player.stats.datatypes.db.PlayerMapStatsRecordKey;
import ru.otus.courses.kafka.player.stats.processor.model.MapStatsAggregateResult;
import ru.otus.courses.kafka.player.stats.processor.model.PlayerStatsAggregateResult;

@RequiredArgsConstructor
public class PlayerMapStatsRecordKeyValueMapper implements
    KeyValueMapper<String, PlayerStatsAggregateResult, List<KeyValue<PlayerMapStatsRecordKey, PlayerMapStatsRecord>>> {

  private final ValueMapper<MapStatsAggregateResult, PlayerMapStatsRecord> valueMapper;

  @Override
  public List<KeyValue<PlayerMapStatsRecordKey, PlayerMapStatsRecord>> apply(String key,
                                                                             PlayerStatsAggregateResult value) {
    return value.getStatsByMap().entrySet().stream()
        .map(entry -> new KeyValue<>(
            PlayerMapStatsRecordKey.newBuilder()
                .setPlayerId(value.getPlayerId())
                .setMap(entry.getKey())
                .build(),
            valueMapper.apply(entry.getValue())))
        .toList();
  }
}
