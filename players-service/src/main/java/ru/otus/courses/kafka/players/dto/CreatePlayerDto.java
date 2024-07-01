package ru.otus.courses.kafka.players.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(title = "Create player request", description = "Create player request DTO")
public class CreatePlayerDto {

  @NotBlank
  @Schema(title = "Login")
  private String login;

  @NotBlank
  @Schema(title = "Email")
  private String email;
}
