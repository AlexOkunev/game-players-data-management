package ru.otus.courses.kafka.battle.results.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "player_battle_results")
public class PlayerBattleResult {

  @EmbeddedId
  private PlayerBattleResultId id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "player_id", insertable = false, updatable = false)
  private Player player;

  @NotNull
  @Column(name = "map_name")
  private String mapName;

  @NotNull
  @Column(name = "event_id")
  private String eventId;

  @NotNull
  @Column(name = "battle_finished_timestamp")
  private ZonedDateTime battleFinishedTimestamp;

  @NotNull
  @Column(name = "damage_sum")
  private Integer damageSum;

  @NotNull
  @Column(name = "shots")
  private Integer shots;

  @NotNull
  @Column(name = "successful_shots")
  private Integer successfulShots;

  @NotNull
  @Column(name = "headshots")
  private Integer headshots;

  @NotNull
  @Column(name = "killed")
  private Integer killed;

  @NotNull
  @Column(name = "deaths")
  private Integer deaths;

  @NotNull
  @Column(name = "winner")
  private Boolean winner = false;
}