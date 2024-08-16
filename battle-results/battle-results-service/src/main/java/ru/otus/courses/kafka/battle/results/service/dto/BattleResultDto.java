package ru.otus.courses.kafka.battle.results.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(title = "Player battle result")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BattleResultDto {

  @Schema(title = "Player ID")
  private Long playerId;

  @Schema(title = "Player login")
  private String login;

  @Schema(title = "Battle ID")
  private Long battleId;

  @Schema(title = "Battle map name")
  private String battleMap;

  @Schema(title = "Battle finished time")
  private ZonedDateTime battleFinishedTime;

  @Schema(title = "Shots count", description = "Number of shots made during the battle")
  private Integer shotsCount;

  @Schema(title = "Successful shots count", description = "Number of successful shots made during the battle")
  private Integer successfulShotsCount;

  @Schema(title = "Headshots count", description = "Number of headshots made during the battle")
  private Integer headshotsCount;

  @Schema(title = "Kills count", description = "Number of kills made during the battle")
  private Integer killsCount;

  @Schema(title = "Deaths count", description = "How many times player was killed during the battle")
  private Integer deathsCount;

  @Schema(title = "Is a winner", description = "Current player won the battle")
  private Boolean winner;

  @Schema(title = "Damage sum", description = "Sum of damage made by player during the battle")
  private Integer damageSum;
}
