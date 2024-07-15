package ru.otus.courses.kafka.battle.results.service.exception;

import java.net.URI;
import lombok.Getter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

@Getter
public class PlayerBattleResultNotFoundException extends AbstractThrowableProblem {

  private static final URI TYPE = URI.create("battle-results-service/player-battle-result/not-found");

  private final long playerId;

  private final long battleId;

  public PlayerBattleResultNotFoundException(long playerId, long battleId) {
    super(TYPE, "Player battle result is not found", Status.NOT_FOUND, "Player %d battle %d result is not found".formatted(playerId, battleId));
    this.playerId = playerId;
    this.battleId = battleId;
  }
}
