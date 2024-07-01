package ru.otus.courses.kafka.battle.results.service.processors;

import static java.util.Optional.ofNullable;
import static ru.otus.courses.kafka.battle.results.service.config.Constants.StateStores.BATTLE_INFO_STATE_STORE;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;
import ru.otus.courses.kafka.battle.results.service.model.BattleInfo;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleEvent;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleResult;

@Slf4j
public class BattleInfoProcessor implements Processor<String, BattleEvent, String, BattleEvent> {

  private ProcessorContext<String, BattleEvent> context;

  private KeyValueStore<String, BattleInfo> battleInfoStore;

  @Override
  public void init(ProcessorContext<String, BattleEvent> context) {
    this.context = context;
    this.battleInfoStore = context.getStateStore(BATTLE_INFO_STATE_STORE);
  }

  @Override
  public void process(Record<String, BattleEvent> record) {
    String battleRecordKey = record.key();
    BattleEvent battleEvent = record.value();

    switch (battleEvent.getType()) {
      case BATTLE_STARTED -> {
        log.info("Battle {} started. Event ID {}", battleRecordKey, battleEvent.getEventId());
        BattleInfo battleInfo = ofNullable(battleInfoStore.get(battleRecordKey)).orElseThrow();
        battleInfo.setBattleId(battleEvent.getBattleId());
        battleInfo.setMap(battleEvent.getBattleInfo().getMap().name());
        battleInfoStore.put(battleRecordKey, battleInfo);
        context.forward(record);
      }
      case BATTLE_FINISHED -> {
        log.info("Battle {} finished. Event ID {}", battleRecordKey, battleEvent.getEventId());
        BattleInfo battleInfo = ofNullable(battleInfoStore.get(battleRecordKey)).orElseThrow();
        ofNullable(battleEvent.getBattleResult())
            .map(BattleResult::getWinners)
            .ifPresent(winners -> {
              battleInfo.setWinners(winners);
              battleInfoStore.put(battleRecordKey, battleInfo);
            });
        context.forward(record);
      }
      case PLAYER_CONNECTED -> {
        log.info("Battle {}. Player connected. Event ID {}", battleEvent.getBattleId(), battleEvent.getEventId());

        ofNullable(battleEvent.getConnectedPlayerInfo()).ifPresentOrElse(connectedPlayer -> {
              BattleInfo battleInfo = ofNullable(battleInfoStore.get(battleRecordKey)).orElseThrow();
              battleInfo.getParticipants().add(connectedPlayer.getPlayerId());
              battleInfoStore.put(battleRecordKey, battleInfo);
            },
            () -> log.error("Event {} {} has no player info", battleEvent.getType(), battleEvent.getEventId()));
      }
      case SHOT_PERFORMED -> context.forward(record);
      case UNKNOWN -> log.error("Skip event with type UNKNOWN and event ID {}", battleEvent.getEventId());
    }
  }
}
