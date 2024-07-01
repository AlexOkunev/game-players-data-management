package ru.otus.courses.kafka.players.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.courses.kafka.players.dto.CreatePlayerDto;
import ru.otus.courses.kafka.players.dto.PlayerDto;
import ru.otus.courses.kafka.players.exception.PlayerNotFoundException;
import ru.otus.courses.kafka.players.service.PlayersService;

@RestController("/players")
@RequiredArgsConstructor
public class PlayerController {

  private final PlayersService playersService;

  @GetMapping("/{id}")
  @Operation(summary = "Get player by ID")
  public PlayerDto getById(@Parameter(description = "Player ID") @PathVariable long id) {
    return playersService.getPlayer(id)
        .orElseThrow(() -> new PlayerNotFoundException(id));
  }

  @PostMapping
  @Operation(summary = "Create player")
  @ResponseStatus(HttpStatus.CREATED)
  public PlayerDto create(@Valid @RequestBody CreatePlayerDto createPlayerDto) {
    return playersService.createPlayer(createPlayerDto);
  }

  @GetMapping
  @Operation(summary = "Get players")
  public List<PlayerDto> getPlayers(@Parameter(description = "Page number", example = "0") @Min(value = 0) int page,
                                    @Parameter(description = "Page size", example = "20") @Min(value = 1) int count) {
    return playersService.getAllPlayers(page, count);
  }

  //TODO use normal pagination
}
