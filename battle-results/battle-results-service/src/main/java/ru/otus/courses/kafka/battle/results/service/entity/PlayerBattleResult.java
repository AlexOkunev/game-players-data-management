package ru.otus.courses.kafka.battle.results.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "player_battle_results", schema = "public")
public class PlayerBattleResult {

  @EmbeddedId
  private PlayerBattleResultId id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "\"playerId\"", insertable = false, updatable = false)
  private Player player;

  @NotNull
  @Column(name = "map", nullable = false, length = Integer.MAX_VALUE)
  private String map;

  @NotNull
  @Column(name = "\"eventId\"", nullable = false, length = Integer.MAX_VALUE)
  private String eventId;

  @NotNull
  @Column(name = "\"battleFinishedTimestamp\"", nullable = false)
  private Long battleFinishedTimestamp;

  @NotNull
  @Column(name = "\"damageSum\"", nullable = false)
  private Integer damageSum;

  @NotNull
  @Column(name = "shots", nullable = false)
  private Integer shots;

  @NotNull
  @Column(name = "\"successfulShots\"", nullable = false)
  private Integer successfulShots;

  @NotNull
  @Column(name = "headshots", nullable = false)
  private Integer headshots;

  @NotNull
  @Column(name = "killed", nullable = false)
  private Integer killed;

  @NotNull
  @Column(name = "deaths", nullable = false)
  private Integer deaths;

  @NotNull
  @Column(name = "winner", nullable = false)
  private Boolean winner = false;

}