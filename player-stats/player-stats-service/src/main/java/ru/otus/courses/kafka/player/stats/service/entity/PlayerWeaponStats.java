package ru.otus.courses.kafka.player.stats.service.entity;

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
@Table(name = "player_weapon_stats")
public class PlayerWeaponStats {

  @EmbeddedId
  private PlayerWeaponStatsId id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "playerId", insertable = false, updatable = false)
  private Player player;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "weaponId", insertable = false, updatable = false)
  private Weapon weapon;

  @NotNull
  @Column(name = "battlesCount", nullable = false)
  private Integer battlesCount;

  @NotNull
  @Column(name = "shotsCount", nullable = false)
  private Integer shotsCount;

  @NotNull
  @Column(name = "successfulShotsCount", nullable = false)
  private Integer successfulShotsCount;

  @NotNull
  @Column(name = "headshotsCount", nullable = false)
  private Integer headshotsCount;

  @NotNull
  @Column(name = "killsCount", nullable = false)
  private Integer killsCount;

  @NotNull
  @Column(name = "damageSum", nullable = false)
  private Integer damageSum;

  @NotNull
  @Column(name = "successfulShotsRate", nullable = false)
  private Float successfulShotsRate;

  @NotNull
  @Column(name = "headshotsToSuccessfulShotsRate", nullable = false)
  private Float headshotsToSuccessfulShotsRate;

  @NotNull
  @Column(name = "avgSuccessfulShotDamage", nullable = false)
  private Float avgSuccessfulShotDamage;

  @NotNull
  @Column(name = "eventId", nullable = false)
  private String eventId;

}