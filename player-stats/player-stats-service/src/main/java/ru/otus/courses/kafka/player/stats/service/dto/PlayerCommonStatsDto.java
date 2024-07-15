package ru.otus.courses.kafka.player.stats.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerCommonStatsDto {

  private Long playerId;

  private String login;

  private Integer battlesCount;

  private Integer shotsCount;

  private Integer successfulShotsCount;

  private Integer headshotsCount;

  private Integer killsCount;

  private Integer deathsCount;

  private Integer winsCount;

  private Integer damageSum;

  private Float successfulShotsRate;

  private Float headshotsToSuccessfulShotsRate;

  private Float avgSuccessfulShotDamage;

  private Float winsRate;
}
