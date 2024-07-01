package ru.otus.courses.kafka.gameserver;

import static java.lang.System.currentTimeMillis;
import static ru.otus.courses.kafka.gameserver.config.Configuration.ADMIN_CONFIG;
import static ru.otus.courses.kafka.gameserver.config.Configuration.PRODUCER_CONFIG;
import static ru.otus.courses.kafka.gameserver.config.Topics.BATTLE_EVENTS;
import static ru.otus.courses.kafka.gameserver.config.Topics.PLAYER_BATTLE_RESULTS;
import static ru.otus.courses.kafka.gameserver.util.AdminUtils.recreateTopics;
import static ru.otus.courses.kafka.gameserver.util.BattleEventsProducerUtils.battleKey;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleEvent;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleEventType;
import ru.otus.courses.kafka.gameserver.generation.BattleGenerator;
import ru.otus.courses.kafka.gameserver.model.GeneratedBattleData;
import ru.otus.courses.kafka.gameserver.model.WeaponDamage;
import ru.otus.courses.kafka.gameserver.util.ProducerCallback;

@Slf4j
public class GameServer {

  private static final WeaponDamage[] WEAPON_DAMAGES = new WeaponDamage[]{
      new WeaponDamage(20, 40),
      new WeaponDamage(35, 60),
      new WeaponDamage(50, 30),
      new WeaponDamage(25, 70)};

  public static void main(String[] args) throws InterruptedException {
//    recreateTopics(ADMIN_CONFIG, 2, 3, BATTLE_EVENTS, PLAYER_BATTLE_RESULTS);
//    Thread.sleep(5000);

    BattleGenerator battleGenerator = new BattleGenerator(WEAPON_DAMAGES, 50, 100, 1000000);
    GeneratedBattleData generatedBattleData = battleGenerator.generate(10, 5, 5, 10, 5, 70, 30);
//    GeneratedBattleData generatedBattleData = battleGenerator.generate(10, 5, 5, 0, 5, 70, 30, -1);

    try (var battleEventsProducer = new KafkaProducer<String, BattleEvent>(PRODUCER_CONFIG);
        var executorService = Executors.newFixedThreadPool(2)) {
      log.info("Send battle events");

      List<BattleEvent> battleEvents = generatedBattleData.battleEvents();
      List<BattleEvent> delayedBattleEvents = generatedBattleData.delayedBattleEvents();

      BattleEvent battleFinishedEvent = battleEvents.stream()
          .filter(battleEvent -> battleEvent.getType() == BattleEventType.BATTLE_FINISHED)
          .findFirst()
          .orElseThrow();

      long initialTimestamp = battleEvents.getFirst().getTimestamp();

      executorService.invokeAll(List.of(
          () -> sendBattleEvents(battleEvents, battleEventsProducer, initialTimestamp),
          () -> sendBattleEventsAfter(delayedBattleEvents, battleEventsProducer, battleFinishedEvent.getTimestamp(), 9))
      );

      log.info("Finish");
    }
  }

  private static Void sendBattleEvents(List<BattleEvent> battleEvents,
                                       KafkaProducer<String, BattleEvent> producer,
                                       long initialTime) {
    try {
      log.info("Send battle {} events", battleEvents.getFirst().getBattleId());

      long previousTimestamp = initialTime;

      for (BattleEvent event : battleEvents) {
        log.info("Wait {} ms to send battle {} event", event.getTimestamp() - previousTimestamp, event.getBattleId());
        Thread.sleep(event.getTimestamp() - previousTimestamp);
        log.info("Send {} battle {} event {}", event.getType(), event.getBattleId(), event.getEventId());
        producer.send(new ProducerRecord<>(BATTLE_EVENTS, battleKey(event), event), ProducerCallback.INSTANCE);
        previousTimestamp = event.getTimestamp();
      }

      log.info("All battle events have been sent");

      return null;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private static Void sendBattleEventsAfter(List<BattleEvent> battleEvents,
                                            KafkaProducer<String, BattleEvent> producer, long initialTime,
                                            long secondsBetweenEvents) {
    try {
      if (battleEvents.isEmpty()) {
        log.info("There are no delayed battle events");
      } else {
        long timeToWait = initialTime - currentTimeMillis();
        log.info("Wait {} seconds to send delayed {} battle events", timeToWait / 1000, battleEvents.size());
        Thread.sleep(timeToWait);

        log.info("Send delayed battle events");

        Random random = new Random(currentTimeMillis());

        for (BattleEvent event : battleEvents) {
          long timeForSleep = random.nextLong(secondsBetweenEvents) + 1;
          log.info("Wait {} seconds to send delayed battle {} event", timeForSleep, event.getBattleId());
          Thread.sleep(Duration.ofSeconds(timeForSleep));
          log.info("Send delayed {} battle {} event {}. {} seconds after finish", event.getType(), event.getBattleId(),
              event.getEventId(), (currentTimeMillis() - initialTime) / 1000);
          producer.send(new ProducerRecord<>(BATTLE_EVENTS, battleKey(event), event), ProducerCallback.INSTANCE);
        }

        log.info("All delayer battle events have been sent");
      }

      return null;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}