package ru.otus.courses.kafka.player.stats.service.entity;

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
public class PlayerMapStatsId implements java.io.Serializable {

  private static final long serialVersionUID = 6921570525394641166L;
  @NotNull
  @Column(name = "map", nullable = false)
  private String map;

  @NotNull
  @Column(name = "playerId", nullable = false)
  private Long playerId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    PlayerMapStatsId entity = (PlayerMapStatsId) o;
    return Objects.equals(this.map, entity.map) &&
        Objects.equals(this.playerId, entity.playerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(map, playerId);
  }

}