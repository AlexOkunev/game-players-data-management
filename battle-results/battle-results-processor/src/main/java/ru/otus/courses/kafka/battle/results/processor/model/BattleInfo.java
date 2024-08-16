package ru.otus.courses.kafka.battle.results.processor.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class BattleInfo {

  private long battleId;

  private Instant battleStartedServerTime;

  private Instant battleFinishedServerTime;

  private Instant processUntilProcessorTime;

  private String map;

  private Set<Long> participants = new HashSet<>();

  private List<Long> winners = List.of();
}
