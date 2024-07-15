package ru.otus.courses.kafka.battle.results.processor.processors;

import static java.util.Optional.ofNullable;
import static ru.otus.courses.kafka.battle.results.processor.util.MappingUtils.toAvroRecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.Punctuator;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;
import ru.otus.courses.kafka.battle.results.datatypes.PlayerBattleTotalResult;
import ru.otus.courses.kafka.battle.results.processor.model.BattleInfo;
import ru.otus.courses.kafka.battle.results.processor.model.PlayerBattleResult;
import ru.otus.courses.kafka.battle.results.processor.model.PlayerBattleResultsKey;

@Slf4j
@RequiredArgsConstructor
public class BattleResultsPunctuator implements Punctuator {

  private final ProcessorContext<String, PlayerBattleTotalResult> context;
  private final KeyValueStore<PlayerBattleResultsKey, PlayerBattleResult> battleResultsStore;
  private final KeyValueStore<String, BattleInfo> battleInfoStore;
  private final KeyValueStore<String, Long> battlesToSendStore;

  @Override
  public void punctuate(long timestamp) {
    try (var iterator = battlesToSendStore.all()) {
      iterator.forEachRemaining(battleToSendEntry -> {
        if (battleToSendEntry.value <= timestamp) {
          log.info("Send battle {} players results", battleToSendEntry.key);

          ofNullable(battleInfoStore.get(battleToSendEntry.key)).stream()
              .flatMap(battleInfo -> battleInfo.getParticipants().stream()
                  .map(playerId -> new PlayerBattleResultsKey(battleInfo.getBattleId(), playerId))
                  .flatMap(resultsKey -> ofNullable(battleResultsStore.get(resultsKey)).stream()
                      .map(playerBattleResult -> new Record<>(String.valueOf(resultsKey.playerId()),
                          toAvroRecord(resultsKey.playerId(), battleInfo, playerBattleResult),
                          context.currentSystemTimeMs()))))
              .forEach(record -> {
                log.info("Send player {} battle {} results", record.value().getPlayerId(),
                    record.value().getBattleId());
                context.forward(record);
              });

          battlesToSendStore.delete(battleToSendEntry.key);
        }
      });
    }
  }
}
