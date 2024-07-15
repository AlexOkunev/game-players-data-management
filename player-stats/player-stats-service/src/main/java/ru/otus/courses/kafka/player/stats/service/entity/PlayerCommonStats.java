package ru.otus.courses.kafka.player.stats.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "player_common_stats")
public class PlayerCommonStats {

  @Id
  @Column(name = "\"playerId\"", nullable = false)
  private Long playerId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "\"playerId\"", insertable = false, updatable = false, referencedColumnName = "id")
  private Player player;

  @NotNull
  @Column(name = "\"battlesCount\"", nullable = false)
  private Integer battlesCount;

  @NotNull
  @Column(name = "\"shotsCount\"", nullable = false)
  private Integer shotsCount;

  @NotNull
  @Column(name = "\"successfulShotsCount\"", nullable = false)
  private Integer successfulShotsCount;

  @NotNull
  @Column(name = "\"headshotsCount\"", nullable = false)
  private Integer headshotsCount;

  @NotNull
  @Column(name = "\"killsCount\"", nullable = false)
  private Integer killsCount;

  @NotNull
  @Column(name = "\"deathsCount\"", nullable = false)
  private Integer deathsCount;

  @NotNull
  @Column(name = "\"winsCount\"", nullable = false)
  private Integer winsCount;

  @NotNull
  @Column(name = "\"damageSum\"", nullable = false)
  private Integer damageSum;

  @NotNull
  @Column(name = "\"successfulShotsRate\"", nullable = false)
  private Float successfulShotsRate;

  @NotNull
  @Column(name = "\"headshotsToSuccessfulShotsRate\"", nullable = false)
  private Float headshotsToSuccessfulShotsRate;

  @NotNull
  @Column(name = "\"avgSuccessfulShotDamage\"", nullable = false)
  private Float avgSuccessfulShotDamage;

  @NotNull
  @Column(name = "\"winsRate\"", nullable = false)
  private Float winsRate;

  @NotNull
  @Column(name = "\"eventId\"", nullable = false, length = Integer.MAX_VALUE)
  private String eventId;

}