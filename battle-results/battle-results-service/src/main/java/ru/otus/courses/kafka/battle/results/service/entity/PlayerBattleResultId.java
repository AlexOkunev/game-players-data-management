package ru.otus.courses.kafka.battle.results.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@Embeddable
public class PlayerBattleResultId implements java.io.Serializable {

  private static final long serialVersionUID = 5450102829316311977L;
  @NotNull
  @Column(name = "\"playerId\"", nullable = false)
  private Long playerId;

  @NotNull
  @Column(name = "\"battleId\"", nullable = false)
  private Long battleId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    PlayerBattleResultId entity = (PlayerBattleResultId) o;
    return Objects.equals(this.battleId, entity.battleId) &&
        Objects.equals(this.playerId, entity.playerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(battleId, playerId);
  }

}