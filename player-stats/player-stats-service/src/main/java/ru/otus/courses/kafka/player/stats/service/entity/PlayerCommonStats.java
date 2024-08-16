package ru.otus.courses.kafka.player.stats.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
@Entity
@Table(name = "player_common_stats")
public class PlayerCommonStats {

  @Id
  @Column(name = "player_id")
  private Long playerId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "player_id", insertable = false, updatable = false, referencedColumnName = "id")
  private Player player;

  @NotNull
  @Column(name = "battles_count")
  private Integer battlesCount;

  @NotNull
  @Column(name = "shots_count")
  private Integer shotsCount;

  @NotNull
  @Column(name = "successful_shots_count")
  private Integer successfulShotsCount;

  @NotNull
  @Column(name = "headshots_count")
  private Integer headshotsCount;

  @NotNull
  @Column(name = "kills_count")
  private Integer killsCount;

  @NotNull
  @Column(name = "deaths_count")
  private Integer deathsCount;

  @NotNull
  @Column(name = "wins_count")
  private Integer winsCount;

  @NotNull
  @Column(name = "damage_sum")
  private Integer damageSum;

  @NotNull
  @Column(name = "successful_shots_rate")
  private Float successfulShotsRate;

  @NotNull
  @Column(name = "headshots_to_successful_shots_rate")
  private Float headshotsToSuccessfulShotsRate;

  @NotNull
  @Column(name = "avg_successful_shot_damage")
  private Float avgSuccessfulShotDamage;

  @NotNull
  @Column(name = "wins_rate")
  private Float winsRate;

  @NotNull
  @Column(name = "event_Id")
  private String eventId;
}