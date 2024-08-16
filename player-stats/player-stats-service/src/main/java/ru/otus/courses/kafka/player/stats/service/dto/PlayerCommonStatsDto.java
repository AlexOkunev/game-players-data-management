package ru.otus.courses.kafka.player.stats.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(title = "Player common statistics")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerCommonStatsDto {

  @Schema(title = "Player ID")
  private Long playerId;

  @Schema(title = "Player login")
  private String login;

  @Schema(title = "Battles count")
  private Integer battlesCount;

  @Schema(title = "Shots count", description = "Number of shots made during all battles")
  private Integer shotsCount;

  @Schema(title = "Successful shots count", description = "Number of successful shots made during all battles")
  private Integer successfulShotsCount;

  @Schema(title = "Headshots count", description = "Number of headshots made during all battles")
  private Integer headshotsCount;

  @Schema(title = "Kills count", description = "Number of kills made during all battles")
  private Integer killsCount;

  @Schema(title = "Deaths count", description = "How many times player was killed during all battles")
  private Integer deathsCount;

  @Schema(title = "Wins count", description = "How many times player became a winner")
  private Integer winsCount;

  @Schema(title = "Damage sum", description = "Sum of damage made by player during all battles")
  private Integer damageSum;

  @Schema(title = "Successful shots rate", description = "Percent of successful shots made by player during all battles")
  private Float successfulShotsRate;

  @Schema(title = "Headshots to successful shots rate", description = "Percent of headshots. 100% - successful shots made by player during all battles")
  private Float headshotsToSuccessfulShotsRate;

  @Schema(title = "Average successful shot damage", description = "Average damage made by player's successful shot in all battles")
  private Float avgSuccessfulShotDamage;

  @Schema(title = "Wins rate", description = "Percent of won battles")
  private Float winsRate;
}