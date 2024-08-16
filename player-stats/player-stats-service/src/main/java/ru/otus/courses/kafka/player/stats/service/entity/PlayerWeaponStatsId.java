package ru.otus.courses.kafka.player.stats.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Embeddable
public class PlayerWeaponStatsId implements java.io.Serializable {

  @NotNull
  @Column(name = "player_id")
  private Long playerId;

  @NotNull
  @Column(name = "weapon_id")
  private Long weaponId;
}