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
      case BATTLE_FINISHED -> context.forward(record);
      case SHOT_PERFORMED -> {
        boolean hasChanges = false;
        log.info("Battle %d. Shot. Event ID %s".formatted(battleEvent.getBattleId(), battleEvent.getEventId()));

        BattleInfo battleInfo = ofNullable(battleInfoStore.get(battleRecordKey)).orElseThrow();

        if (!battleInfo.getParticipants().contains(battleEvent.getShotInfo().getShooterPlayerId())) {
          log.info("Battle {} new participant {}. Event ID {}", battleRecordKey,
              battleEvent.getShotInfo().getShooterPlayerId(), battleEvent.getEventId());
          battleInfo.getParticipants().add(battleEvent.getShotInfo().getShooterPlayerId());
          hasChanges = true;
        }

        if (battleEvent.getShotInfo().getVictimPlayerId() != null) {
          if (!battleInfo.getParticipants().contains(battleEvent.getShotInfo().getVictimPlayerId())) {
            log.info("Battle {} new participant {}. Event ID {}", battleRecordKey,
                battleEvent.getShotInfo().getVictimPlayerId(), battleEvent.getEventId());
            battleInfo.getParticipants().add(battleEvent.getShotInfo().getVictimPlayerId());
            hasChanges = true;
          }
        }

        if (hasChanges) {
          log.info("Battle {} has changes. Event ID {}", battleRecordKey, battleEvent.getEventId());
          battleInfoStore.put(battleRecordKey, battleInfo);
        }

        context.forward(record);
      }
      case UNKNOWN -> log.info("Skip event with type UNKNOWN and event ID {}", battleEvent.getEventId());
    }
  }
}
