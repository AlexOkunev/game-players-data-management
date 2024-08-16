package ru.otus.courses.kafka.players.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.Data;

@Data
@Schema(title = "Player", description = "Player response DTO")
public class PlayerDto {

  @Schema(title = "Player ID")
  private long id;

  @Schema(title = "Login")
  private String login;

  @Schema(title = "Email")
  private String email;

  @Schema(title = "Player is active")
  private boolean active;

  @Schema(title = "Created datetime")
  private ZonedDateTime created;

  @Schema(title = "Last update datetime")
  private ZonedDateTime updated;
}
