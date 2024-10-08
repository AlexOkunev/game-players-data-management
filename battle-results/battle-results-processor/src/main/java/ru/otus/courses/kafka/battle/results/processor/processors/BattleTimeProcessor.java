package ru.otus.courses.kafka.battle.results.processor.processors;

import static java.util.Optional.ofNullable;
import static ru.otus.courses.kafka.battle.results.processor.config.Constants.StateStores.BATTLE_INFO_STATE_STORE;

import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;
import ru.otus.courses.kafka.battle.results.processor.model.BattleInfo;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleEvent;

@Slf4j
public class BattleTimeProcessor implements Processor<String, BattleEvent, String, BattleEvent> {

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
    BattleEvent event = record.value();

    switch (event.getType()) {
      case BATTLE_STARTED -> {
        log.info("Battle {} started. Event ID {}", battleRecordKey, event.getEventId());
        BattleInfo battleInfo = ofNullable(battleInfoStore.get(battleRecordKey)).orElseGet(BattleInfo::new);
        battleInfo.setBattleStartedServerTime(event.getTimestamp());
        battleInfoStore.put(battleRecordKey, battleInfo);
        context.forward(record);
      }
      case BATTLE_FINISHED -> {
        log.info("Battle {} finished. Event ID {}", battleRecordKey, event.getEventId());
        BattleInfo battleInfo = ofNullable(battleInfoStore.get(battleRecordKey)).orElseGet(BattleInfo::new);
        battleInfo.setBattleFinishedServerTime(event.getTimestamp()); //TODO time to config - 30 seconds
        battleInfo.setProcessUntilProcessorTime(Instant.now().plusSeconds(30));
        battleInfoStore.put(battleRecordKey, battleInfo);
        context.forward(record);
      }
      case SHOT_PERFORMED, PLAYER_CONNECTED -> {
        log.info("Battle {} event {}. Event ID {}", battleRecordKey, event.getType(), event.getEventId());
        ofNullable(battleInfoStore.get(battleRecordKey))
            .ifPresentOrElse(battleInfo -> {
                  if (battleGoesOn(event, battleInfo) && battleCanBeProcessed(event, battleInfo)) {
                    context.forward(record);
                  }
                },
                () -> log.error("Event before battle {} start. Event ID {}", battleRecordKey, event.getEventId()));
      }
      case UNKNOWN -> {
        log.error("Unknown event type. Don't process it. Event ID {}", event.getEventId());
      }
    }
  }

  private boolean battleGoesOn(BattleEvent battleEvent, BattleInfo battleInfo) {
    Instant eventTimestamp = battleEvent.getTimestamp();
    Instant battleStartTimestamp = battleInfo.getBattleStartedServerTime();
    Instant battleFinishTimestamp = battleInfo.getBattleFinishedServerTime();

    boolean result = battleStartTimestamp != null && !eventTimestamp.isBefore(battleStartTimestamp)
        && (battleFinishTimestamp == null || !eventTimestamp.isAfter(battleFinishTimestamp));

    if (!result) {
      log.info("Event {} has incorrect timestamp {}. Battle {} time is {} - {}", battleEvent.getEventId(),
          eventTimestamp, battleEvent.getBattleId(), battleStartTimestamp, battleFinishTimestamp);
    }

    return result;
  }

  private boolean battleCanBeProcessed(BattleEvent battleEvent, BattleInfo battleInfo) {
    boolean result = battleInfo.getProcessUntilProcessorTime() == null
        || battleInfo.getProcessUntilProcessorTime().isAfter(Instant.ofEpochMilli(context.currentSystemTimeMs()));

    if (battleInfo.getProcessUntilProcessorTime() != null) {
      log.info("Current system time is {}. Battle {} must be processed until {}. Result: {}. Event ID {}",
          Instant.ofEpochMilli(context.currentSystemTimeMs()).toString(), battleEvent.getBattleId(),
          battleInfo.getProcessUntilProcessorTime().toString(), result, battleEvent.getEventId());
    } else {
      log.info("Current system time is {}. Battle can be processed. Event ID {}",
          Instant.ofEpochMilli(context.currentSystemTimeMs()).toString(), battleEvent.getEventId());
    }

    if (!result) {
      log.info("Event {} can not be processed. Grace period has finished", battleEvent.getEventId());
    }

    return result;
  }
}
