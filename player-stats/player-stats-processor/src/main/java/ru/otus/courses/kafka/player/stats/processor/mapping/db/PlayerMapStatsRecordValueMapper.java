package ru.otus.courses.kafka.player.stats.processor.mapping.db;

import java.util.UUID;
import org.apache.kafka.streams.kstream.ValueMapper;
import ru.otus.courses.kafka.player.stats.datatypes.db.PlayerMapStatsRecord;
import ru.otus.courses.kafka.player.stats.processor.model.MapStatsAggregateResult;

public class PlayerMapStatsRecordValueMapper implements
    ValueMapper<MapStatsAggregateResult, PlayerMapStatsRecord> {

  @Override
  public PlayerMapStatsRecord apply(MapStatsAggregateResult aggregateResult) {
    return PlayerMapStatsRecord.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setBattlesCount(aggregateResult.getBattlesCount())
        .setShotsCount(aggregateResult.getShotsCount())
        .setSuccessfulShotsCount(aggregateResult.getSuccessfulShotsCount())
        .setHeadshotsCount(aggregateResult.getHeadshotsCount())
        .setDamageSum(aggregateResult.getDamageSum())
        .setKillsCount(aggregateResult.getKillsCount())
        .setDeathsCount(aggregateResult.getDeathsCount())
        .setWinsCount(aggregateResult.getWinsCount())
        .setSuccessfulShotsRate(aggregateResult.getSuccessfulShotsRate())
        .setHeadshotsToSuccessfulShotsRate(aggregateResult.getHeadshotsToSuccessfulShotsRate())
        .setWinsRate(aggregateResult.getWinsRate())
        .setAvgSuccessfulShotDamage(aggregateResult.getAvgSuccessfulShotDamage())
        .build();
  }
}
