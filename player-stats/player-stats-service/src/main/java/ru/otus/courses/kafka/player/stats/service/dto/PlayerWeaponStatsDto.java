package ru.otus.courses.kafka.player.stats.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(title = "Player statistics by weapon")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerWeaponStatsDto {

  @Schema(title = "Player ID")
  private Long playerId;

  @Schema(title = "Player login")
  private String login;

  @Schema(title = "Weapon ID")
  private Long weaponId;

  @Schema(title = "Weapon name")
  private String weaponName;

  @Schema(title = "Battles count with given weapon")
  private Integer battlesCount;

  @Schema(title = "Shots count", description = "Number of shots made during all battles with given weapon")
  private Integer shotsCount;

  @Schema(title = "Successful shots count", description = "Number of successful shots made during all battles with given weapon")
  private Integer successfulShotsCount;

  @Schema(title = "Headshots count", description = "Number of headshots made during all battles with given weapon")
  private Integer headshotsCount;

  @Schema(title = "Kills count", description = "Number of kills made during all battles with given weapon")
  private Integer killsCount;

  @Schema(title = "Damage sum", description = "Sum of damage made by player during all battles with given weapon")
  private Integer damageSum;

  @Schema(title = "Successful shots rate", description = "Percent of successful shots made by player during all battles with given weapon")
  private Float successfulShotsRate;

  @Schema(title = "Headshots to successful shots rate", description = "Percent of headshots. 100% - successful shots made by player during all battles with given weapon")
  private Float headshotsToSuccessfulShotsRate;

  @Schema(title = "Average successful shot damage", description = "Average damage made by player's successful shot in all battles with given weapon")
  private Float avgSuccessfulShotDamage;
}