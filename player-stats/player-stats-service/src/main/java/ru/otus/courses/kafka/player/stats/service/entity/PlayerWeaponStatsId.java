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
public class PlayerWeaponStatsId implements java.io.Serializable {

  private static final long serialVersionUID = -740777441093554525L;
  @NotNull
  @Column(name = "playerId", nullable = false)
  private Long playerId;

  @NotNull
  @Column(name = "weaponId", nullable = false)
  private Long weaponId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    PlayerWeaponStatsId entity = (PlayerWeaponStatsId) o;
    return Objects.equals(this.weaponId, entity.weaponId) &&
        Objects.equals(this.playerId, entity.playerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(weaponId, playerId);
  }

}