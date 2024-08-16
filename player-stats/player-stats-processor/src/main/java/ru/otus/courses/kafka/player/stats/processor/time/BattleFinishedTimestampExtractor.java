package ru.otus.courses.kafka.player.stats.processor.time;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import ru.otus.courses.kafka.battle.results.datatypes.PlayerBattleTotalResult;

public class BattleFinishedTimestampExtractor implements TimestampExtractor {

  @Override
  public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
    if (record != null && record.value() != null) {
      if (record.value() instanceof PlayerBattleTotalResult playerBattleTotalResult) {
        return playerBattleTotalResult.getBattleFinishedTimestamp().toEpochMilli();
      }
    }

    return partitionTime;
  }
}
