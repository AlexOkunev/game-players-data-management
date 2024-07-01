package ru.otus.courses.kafka.player.stats.processor.mapping;

import java.util.List;
import java.util.UUID;
import org.apache.kafka.streams.kstream.ValueMapper;
import ru.otus.courses.kafka.player.stats.datatypes.PlayerCommonStatsValues;
import ru.otus.courses.kafka.player.stats.datatypes.PlayerMapStatsListElement;
import ru.otus.courses.kafka.player.stats.datatypes.PlayerStats;
import ru.otus.courses.kafka.player.stats.datatypes.PlayerWeaponStatsListElement;
import ru.otus.courses.kafka.player.stats.processor.model.PlayerStatsAggregateResult;

public class PlayerStatsValueMapper implements ValueMapper<PlayerStatsAggregateResult, PlayerStats> {

  @Override
  public PlayerStats apply(PlayerStatsAggregateResult aggregateResult) {
    PlayerCommonStatsValues playerCommonStats = PlayerCommonStatsValues.newBuilder()
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

    List<PlayerMapStatsListElement> statsByMap = aggregateResult.getStatsByMap().entrySet().stream()
        .map(entry -> PlayerMapStatsListElement.newBuilder()
            .setMap(entry.getKey())
            .setBattlesCount(entry.getValue().getBattlesCount())
            .setShotsCount(entry.getValue().getShotsCount())
            .setSuccessfulShotsCount(entry.getValue().getSuccessfulShotsCount())
            .setHeadshotsCount(entry.getValue().getHeadshotsCount())
            .setDamageSum(entry.getValue().getDamageSum())
            .setKillsCount(entry.getValue().getKillsCount())
            .setDeathsCount(entry.getValue().getDeathsCount())
            .setWinsCount(entry.getValue().getWinsCount())
            .setSuccessfulShotsRate(entry.getValue().getSuccessfulShotsRate())
            .setHeadshotsToSuccessfulShotsRate(entry.getValue().getHeadshotsToSuccessfulShotsRate())
            .setWinsRate(entry.getValue().getWinsRate())
            .setAvgSuccessfulShotDamage(entry.getValue().getAvgSuccessfulShotDamage())
            .build())
        .toList();

    List<PlayerWeaponStatsListElement> statsByWeapon = aggregateResult.getStatsByWeapon().entrySet().stream()
        .map(entry -> PlayerWeaponStatsListElement.newBuilder()
            .setWeaponId(entry.getKey())
            .setBattlesCount(entry.getValue().getBattlesCount())
            .setShotsCount(entry.getValue().getShotsCount())
            .setSuccessfulShotsCount(entry.getValue().getSuccessfulShotsCount())
            .setHeadshotsCount(entry.getValue().getHeadshotsCount())
            .setDamageSum(entry.getValue().getDamageSum())
            .setKillsCount(entry.getValue().getKillsCount())
            .setSuccessfulShotsRate(entry.getValue().getSuccessfulShotsRate())
            .setHeadshotsToSuccessfulShotsRate(entry.getValue().getHeadshotsToSuccessfulShotsRate())
            .setAvgSuccessfulShotDamage(entry.getValue().getAvgSuccessfulShotDamage())
            .build())
        .toList();

    return PlayerStats.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setPlayerId(aggregateResult.getPlayerId())
        .setCommonStats(playerCommonStats)
        .setWeaponStats(statsByWeapon)
        .setMapStats(statsByMap)
        .build();
  }
}
