package ru.otus.courses.kafka.player.stats.service.exception;

import java.net.URI;
import lombok.Getter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

@Getter
public class PlayerStatsNotFoundException extends AbstractThrowableProblem {

  private static final URI TYPE = URI.create("player-stats-service/player-stats/not-found");

  private final long id;

  public PlayerStatsNotFoundException(long id) {
    super(TYPE, "Player stats not found", Status.NOT_FOUND, "Player %d stats not found".formatted(id));
    this.id = id;
  }
}
