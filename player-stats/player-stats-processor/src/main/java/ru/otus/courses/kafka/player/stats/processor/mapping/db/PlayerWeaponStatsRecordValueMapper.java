package ru.otus.courses.kafka.player.stats.processor.mapping.db;

import java.util.UUID;
import org.apache.kafka.streams.kstream.ValueMapper;
import ru.otus.courses.kafka.player.stats.datatypes.db.PlayerWeaponStatsRecord;
import ru.otus.courses.kafka.player.stats.processor.model.WeaponStatsAggregateResult;

public class PlayerWeaponStatsRecordValueMapper implements
    ValueMapper<WeaponStatsAggregateResult, PlayerWeaponStatsRecord> {

  @Override
  public PlayerWeaponStatsRecord apply(WeaponStatsAggregateResult aggregateResult) {
    return PlayerWeaponStatsRecord.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setBattlesCount(aggregateResult.getBattlesCount())
        .setShotsCount(aggregateResult.getShotsCount())
        .setSuccessfulShotsCount(aggregateResult.getSuccessfulShotsCount())
        .setHeadshotsCount(aggregateResult.getHeadshotsCount())
        .setDamageSum(aggregateResult.getDamageSum())
        .setKillsCount(aggregateResult.getKillsCount())
        .setSuccessfulShotsRate(aggregateResult.getSuccessfulShotsRate())
        .setHeadshotsToSuccessfulShotsRate(aggregateResult.getHeadshotsToSuccessfulShotsRate())
        .setAvgSuccessfulShotDamage(aggregateResult.getAvgSuccessfulShotDamage())
        .build();
  }
}
