package ru.otus.courses.kafka.player.stats.processor.aggregators;

import org.apache.kafka.streams.kstream.Aggregator;
import ru.otus.courses.kafka.battle.results.datatypes.PlayerBattleTotalResult;
import ru.otus.courses.kafka.battle.results.datatypes.WeaponResult;
import ru.otus.courses.kafka.player.stats.processor.model.MapStatsAggregateResult;
import ru.otus.courses.kafka.player.stats.processor.model.PlayerStatsAggregateResult;
import ru.otus.courses.kafka.player.stats.processor.model.WeaponStatsAggregateResult;

public class PlayerStatsAggregator implements Aggregator<String, PlayerBattleTotalResult, PlayerStatsAggregateResult> {

  @Override
  public PlayerStatsAggregateResult apply(String key, PlayerBattleTotalResult battleResult,
                                          PlayerStatsAggregateResult aggregateResult) {
    if (aggregateResult.getPlayerId() == null) {
      aggregateResult.setPlayerId(battleResult.getPlayerId());
    } else {
      if (battleResult.getPlayerId() != aggregateResult.getPlayerId()) {
        throw new RuntimeException("Incorrect player id. Aggregate player id: %d. Event player id %d. Event id %s"
            .formatted(aggregateResult.getPlayerId(), battleResult.getPlayerId(), battleResult.getEventId()));
      }
    }

    populateCommonStats(battleResult, aggregateResult);
    populateMapStats(battleResult, aggregateResult.getStatsByMap(battleResult.getMap()));

    battleResult.getWeaponResults().forEach(
        weaponResult -> populateWeaponStats(weaponResult,
            aggregateResult.getStatsByWeaponId(weaponResult.getWeaponId())));

    return aggregateResult;
  }

  private static void populateMapStats(PlayerBattleTotalResult battleResult, MapStatsAggregateResult aggregateResult) {
    aggregateResult.incBattlesCount();
    aggregateResult.addShotsCount(battleResult.getShots());
    aggregateResult.addSuccessfulShotsCount(battleResult.getSuccessfulShots());
    aggregateResult.addHeadshotsCount(battleResult.getHeadshots());
    aggregateResult.addDeathsCount(battleResult.getDeaths());
    aggregateResult.addKillsCount(battleResult.getKilled());
    aggregateResult.addDamage(battleResult.getDamageSum());

    if (battleResult.getWinner()) {
      aggregateResult.incWinsCount();
    }
  }

  private static void populateCommonStats(PlayerBattleTotalResult battleResult,
                                          PlayerStatsAggregateResult aggregateResult) {
    aggregateResult.incBattlesCount();
    aggregateResult.addShotsCount(battleResult.getShots());
    aggregateResult.addSuccessfulShotsCount(battleResult.getSuccessfulShots());
    aggregateResult.addHeadshotsCount(battleResult.getHeadshots());
    aggregateResult.addDeathsCount(battleResult.getDeaths());
    aggregateResult.addKillsCount(battleResult.getKilled());
    aggregateResult.addDamage(battleResult.getDamageSum());

    if (battleResult.getWinner()) {
      aggregateResult.incWinsCount();
    }
  }

  private static void populateWeaponStats(WeaponResult weaponResult,
                                          WeaponStatsAggregateResult aggregateResult) {
    aggregateResult.incBattlesCount();
    aggregateResult.addShotsCount(weaponResult.getShots());
    aggregateResult.addSuccessfulShotsCount(weaponResult.getSuccessfulShots());
    aggregateResult.addHeadshotsCount(weaponResult.getHeadshots());
    aggregateResult.addKillsCount(weaponResult.getKilled());
    aggregateResult.addDamage(weaponResult.getDamage());
  }
}
