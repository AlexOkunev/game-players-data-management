package ru.otus.courses.kafka.util;

import static java.time.Instant.ofEpochMilli;
import static java.time.ZoneOffset.ofHours;
import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

@Slf4j
public class ProducerCallback implements Callback {

  public static Callback INSTANCE = new ProducerCallback();

  public static final int ZONE_OFFSET = 5;

  @Override
  public void onCompletion(RecordMetadata metadata, Exception e) {
    if (e != null) {
      log.error(e.getMessage(), e);
    } else {
      var timestampStr = ISO_ZONED_DATE_TIME.format(ofEpochMilli(metadata.timestamp()).atZone(ofHours(ZONE_OFFSET)));
      log.info("Successfully sent. Topic {}, partition {}, offset {}, timestamp {}",
          metadata.topic(), metadata.partition(), metadata.offset(), timestampStr);
    }
  }
}
