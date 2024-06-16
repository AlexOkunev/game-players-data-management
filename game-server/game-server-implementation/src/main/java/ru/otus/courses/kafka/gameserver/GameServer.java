package ru.otus.courses.kafka.gameserver;

import static ru.otus.courses.kafka.util.Configuration.PRODUCER_CONFIG;
import static ru.otus.courses.kafka.util.Configuration.TOPIC_1;

import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.otus.courses.kafka.game.server.datatypes.MatchMap;
import ru.otus.courses.kafka.game.server.datatypes.MatchStarted;
import ru.otus.courses.kafka.util.ProducerCallback;

@Slf4j
public class GameServer {

  public static void main(String[] args) throws InterruptedException {
//    recreateTopics(ADMIN_CONFIG, 2, 3, TOPIC_1);

    log.info("Create producer");

    MatchStarted matchStartedEvent = new MatchStarted();
    matchStartedEvent.setId("100");
    matchStartedEvent.setMap(MatchMap.CS_ITALY);
    matchStartedEvent.setTimestamp(Instant.now().toEpochMilli());

    try (var producer = new KafkaProducer<String, MatchStarted>(PRODUCER_CONFIG)) {
      log.info("Send");
      producer.send(new ProducerRecord<>(TOPIC_1, matchStartedEvent.getId(), matchStartedEvent),
          ProducerCallback.INSTANCE);
    }

    log.info("Wait");

    Thread.sleep(5000);

    log.info("Finish");
  }
}