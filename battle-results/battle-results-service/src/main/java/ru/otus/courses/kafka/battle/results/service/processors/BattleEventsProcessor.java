package ru.otus.courses.kafka.battle.results.service.processors;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.time.DateFormatUtils.format;
import static ru.otus.courses.kafka.battle.results.service.config.Constants.StateStores.BATTLES_TO_SEND_STATE_STORE;
import static ru.otus.courses.kafka.battle.results.service.config.Constants.StateStores.BATTLE_INFO_STATE_STORE;
import static ru.otus.courses.kafka.battle.results.service.config.Constants.StateStores.BATTLE_RESULTS_STATE_STORE;
import static ru.otus.courses.kafka.battle.results.service.util.TimeUtils.epochMilliPlusSeconds;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;
import ru.otus.courses.kafka.battle.results.datatypes.PlayerBattleTotalResult;
import ru.otus.courses.kafka.battle.results.service.model.BattleInfo;
import ru.otus.courses.kafka.battle.results.service.model.PlayerBattleResult;
import ru.otus.courses.kafka.battle.results.service.model.PlayerBattleResultsKey;
import ru.otus.courses.kafka.battle.results.service.model.PlayerBattleWeaponResult;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleEvent;
import ru.otus.courses.kafka.game.server.datatypes.events.ShotInfo;

@Slf4j
public class BattleEventsProcessor implements
    Processor<String, BattleEvent, String, PlayerBattleTotalResult> {

  private ProcessorContext<String, PlayerBattleTotalResult> context;

  private KeyValueStore<PlayerBattleResultsKey, PlayerBattleResult> battleResultsStore;
  private KeyValueStore<String, Long> battlesToSendStore;

  @Override
  public void init(ProcessorContext<String, PlayerBattleTotalResult> context) {
    this.context = context;
    this.battleResultsStore = context.getStateStore(BATTLE_RESULTS_STATE_STORE);
    this.battlesToSendStore = context.getStateStore(BATTLES_TO_SEND_STATE_STORE);

    //TODO time to config
    KeyValueStore<String, BattleInfo> battleInfoStore = context.getStateStore(BATTLE_INFO_STATE_STORE);
    this.context.schedule(Duration.ofSeconds(10), PunctuationType.WALL_CLOCK_TIME,
        new BattleResultsPunctuator(context, battleResultsStore, battleInfoStore, battlesToSendStore));
  }


  @Override
  public void process(Record<String, BattleEvent> record) {
    String battleRecordKey = record.key();
    BattleEvent battleEvent = record.value();

    switch (battleEvent.getType()) {
      case BATTLE_STARTED, UNKNOWN ->
          log.info("Skip event with ID {} and type {}", record.value().getEventId(), battleEvent.getType());
      case BATTLE_FINISHED ->
          battlesToSendStore.put(battleRecordKey, epochMilliPlusSeconds(context.currentSystemTimeMs(), 40));
      case SHOT_PERFORMED -> {
        ShotInfo shotInfo = battleEvent.getShotInfo();

        if (shotInfo != null) {
          log.info("Battle %d. Shot %d -> %d at %s (%d). Event ID %s".formatted(
              battleEvent.getBattleId(), shotInfo.getShooterPlayerId(), shotInfo.getVictimPlayerId(),
              format(battleEvent.getTimestamp(), "yyyy-MM-dd HH:mm:ss"),
              battleEvent.getTimestamp(), battleEvent.getEventId()));

          PlayerBattleResultsKey storeKey = new PlayerBattleResultsKey(battleEvent.getBattleId(),
              shotInfo.getShooterPlayerId());

          PlayerBattleResult playerBattleResult = ofNullable(battleResultsStore.get(storeKey)).orElseGet(
              PlayerBattleResult::new);

          PlayerBattleWeaponResult playerBattleWeaponResult = playerBattleResult.getWeaponResult(
              shotInfo.getWeaponId());
          playerBattleWeaponResult.addShot(shotInfo.getVictimPlayerId(), shotInfo.getDamage());

          if (shotInfo.getKilled()) {
            playerBattleWeaponResult.incKillsCount();
          }

          if (shotInfo.getHeadshot()) {
            playerBattleWeaponResult.incHeadshotsCount();
          }

          battleResultsStore.put(storeKey, playerBattleResult);

          if (shotInfo.getKilled()) {
            PlayerBattleResultsKey victimStoreKey = new PlayerBattleResultsKey(battleEvent.getBattleId(),
                shotInfo.getVictimPlayerId());
            PlayerBattleResult victimPlayerResult = ofNullable(battleResultsStore.get(victimStoreKey)).orElseGet(
                PlayerBattleResult::new);
            victimPlayerResult.incDeathsCount();
            battleResultsStore.put(victimStoreKey, victimPlayerResult);
          }
        } else {
          log.info("Battle %d. Invalid shot event. Shot info is absent. Event ID %s"
              .formatted(battleEvent.getBattleId(), battleEvent.getEventId()));
        }
      }
    }
  }
}
