package ru.otus.courses.kafka.battle.results.service.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class BattleInfo {

  private long battleId;

  private Long battleStartedServerTime;

  private Long battleFinishedServerTime;

  private Long processUntilProcessorTime;

  private String map;

  private Set<Long> participants = new HashSet<>();
}
