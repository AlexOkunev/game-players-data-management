package ru.otus.courses.kafka.players.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.courses.kafka.players.dto.CreatePlayerRequestDto;
import ru.otus.courses.kafka.players.dto.PageWrappingDTO;
import ru.otus.courses.kafka.players.dto.PlayerDto;
import ru.otus.courses.kafka.players.exception.PlayerNotFoundException;
import ru.otus.courses.kafka.players.service.PlayersService;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

  private final PlayersService playersService;

  @GetMapping("/player/{id}")
  @Operation(summary = "Get player by ID")
  public PlayerDto getById(@Parameter(description = "Player ID") @PathVariable long id) {
    return playersService.getPlayer(id)
        .orElseThrow(() -> new PlayerNotFoundException(id));
  }

  @PostMapping
  @Operation(summary = "Create player")
  @ResponseStatus(HttpStatus.CREATED)
  public PlayerDto create(@Valid @RequestBody CreatePlayerRequestDto createPlayerRequestDto) {
    return playersService.createPlayer(createPlayerRequestDto);
  }

  @GetMapping
  @Operation(summary = "Get players list page")
  public PageWrappingDTO<PlayerDto> getPlayers(
      @Parameter(description = "Page number", example = "0") @Min(value = 0) int page,
      @Parameter(description = "Page size", example = "20") @Min(value = 1) int count) {
    return new PageWrappingDTO<>(playersService.getAllPlayers(page, count));
  }
}
