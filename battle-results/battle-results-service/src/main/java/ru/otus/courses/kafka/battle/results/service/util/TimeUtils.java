package ru.otus.courses.kafka.battle.results.service.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtils {

  public static long epochMilliPlusSeconds(long time, long seconds) {
    return Instant.ofEpochMilli(time).plus(seconds, ChronoUnit.SECONDS).toEpochMilli();
  }
}
