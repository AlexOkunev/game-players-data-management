package ru.otus.courses.kafka.battle.results.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Embeddable
public class PlayerBattleResultId implements java.io.Serializable {

  @NotNull
  @Column(name = "player_id")
  private Long playerId;

  @NotNull
  @Column(name = "battle_id")
  private Long battleId;
}