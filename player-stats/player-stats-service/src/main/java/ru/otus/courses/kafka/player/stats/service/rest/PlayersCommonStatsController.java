package ru.otus.courses.kafka.player.stats.service.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.courses.kafka.player.stats.service.dto.PageWrappingDTO;
import ru.otus.courses.kafka.player.stats.service.dto.PlayerCommonStatsDto;
import ru.otus.courses.kafka.player.stats.service.enumeration.SortProperty;
import ru.otus.courses.kafka.player.stats.service.exception.PlayerStatsNotFoundException;
import ru.otus.courses.kafka.player.stats.service.service.PlayerCommonStatsService;

@RestController
@RequiredArgsConstructor
public class PlayersCommonStatsController {

  private final PlayerCommonStatsService playerCommonStatsService;

  @GetMapping("/players/stats/common")
  @Operation(summary = "Get players common stats")
  public PageWrappingDTO<PlayerCommonStatsDto> getPlayersStats(
      @Parameter(description = "Page number", example = "0") @Min(value = 0) int page,
      @Parameter(description = "Page size", example = "20") @Min(value = 1) int count,
      @Parameter SortProperty sortProperty, @Parameter Direction direction) {
    return new PageWrappingDTO<>(playerCommonStatsService.getPlayersCommonStats(page, count, sortProperty, direction));
  }

  @GetMapping("/players/{playerId}/stats/common")
  @Operation(summary = "Get single player common stats")
  public PlayerCommonStatsDto getPlayerStats(@PathVariable long playerId) {
    return playerCommonStatsService.getPlayerCommonStats(playerId)
        .orElseThrow(() -> new PlayerStatsNotFoundException(playerId));
  }
}
