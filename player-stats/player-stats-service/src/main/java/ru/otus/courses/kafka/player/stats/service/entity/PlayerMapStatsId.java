package ru.otus.courses.kafka.player.stats.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Embeddable
public class PlayerMapStatsId implements java.io.Serializable {

  @NotNull
  @Column(name = "map_name")
  private String mapName;

  @NotNull
  @Column(name = "player_id")
  private Long playerId;
}