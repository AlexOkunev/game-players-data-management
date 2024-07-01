package ru.otus.courses.kafka.player.stats.processor.mapping.db;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.ValueMapper;
import ru.otus.courses.kafka.player.stats.datatypes.db.PlayerWeaponStatsRecord;
import ru.otus.courses.kafka.player.stats.datatypes.db.PlayerWeaponStatsRecordKey;
import ru.otus.courses.kafka.player.stats.processor.model.PlayerStatsAggregateResult;
import ru.otus.courses.kafka.player.stats.processor.model.WeaponStatsAggregateResult;

@RequiredArgsConstructor
public class PlayerWeaponStatsRecordKeyValueMapper implements
    KeyValueMapper<String, PlayerStatsAggregateResult, List<KeyValue<PlayerWeaponStatsRecordKey, PlayerWeaponStatsRecord>>> {

  private final ValueMapper<WeaponStatsAggregateResult, PlayerWeaponStatsRecord> valueMapper;

  @Override
  public List<KeyValue<PlayerWeaponStatsRecordKey, PlayerWeaponStatsRecord>> apply(String key,
                                                                                   PlayerStatsAggregateResult value) {
    return value.getStatsByWeapon().entrySet().stream()
        .map(entry -> new KeyValue<>(
            PlayerWeaponStatsRecordKey.newBuilder()
                .setPlayerId(value.getPlayerId())
                .setWeaponId(entry.getKey())
                .build(),
            valueMapper.apply(entry.getValue())))
        .toList();
  }
}
