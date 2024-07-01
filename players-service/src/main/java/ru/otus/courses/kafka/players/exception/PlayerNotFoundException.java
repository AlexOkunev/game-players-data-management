package ru.otus.courses.kafka.players.exception;

import java.net.URI;
import lombok.Getter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

@Getter
public class PlayerNotFoundException extends AbstractThrowableProblem {

  private static final URI TYPE = URI.create("players-service/player/not-found");

  private final long id;

  public PlayerNotFoundException(long id) {
    super(TYPE, "Player not found", Status.NOT_FOUND, "Player %d not found".formatted(id));
    this.id = id;
  }
}
