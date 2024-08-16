package ru.otus.courses.kafka.player.stats.service.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.courses.kafka.player.stats.service.dto.PageWrappingDTO;
import ru.otus.courses.kafka.player.stats.service.dto.PlayerMapStatsDto;
import ru.otus.courses.kafka.player.stats.service.enumeration.PlayerMapStatsSortProperty;
import ru.otus.courses.kafka.player.stats.service.exception.PlayerStatsNotFoundException;
import ru.otus.courses.kafka.player.stats.service.service.PlayerMapStatsService;

@RestController
@RequiredArgsConstructor
public class PlayersMapStatsController {

  private final PlayerMapStatsService playerMapStatsService;

  @GetMapping("/players/stats/map/{mapName}")
  @Operation(summary = "Get players map stats")
  public PageWrappingDTO<PlayerMapStatsDto> getPlayersMapStats(
      @Parameter(description = "Page number", example = "0") @Min(value = 0) int page,
      @Parameter(description = "Page size", example = "20") @Min(value = 1) int count,
      @Parameter PlayerMapStatsSortProperty sortProperty, @Parameter Direction direction,
      @Parameter(description = "Map name", example = "CS_ITALY") @PathVariable String mapName) {
    return new PageWrappingDTO<>(
        playerMapStatsService.getPlayersMapStats(mapName, page, count, sortProperty, direction));
  }

  @GetMapping("/players/{playerId}/stats/map/{mapName}")
  @Operation(summary = "Get single player map stats for single map")
  public PlayerMapStatsDto getPlayerMapStats(
      @Parameter(description = "Player ID", example = "1") @PathVariable long playerId,
      @Parameter(description = "Map name", example = "CS_ITALY") @PathVariable String mapName) {
    return playerMapStatsService.getPlayerMapStats(playerId, mapName)
        .orElseThrow(() -> new PlayerStatsNotFoundException(playerId));
  }

  @GetMapping("/players/{playerId}/stats/map")
  @Operation(summary = "Get single player map stats for all maps")
  public List<PlayerMapStatsDto> getPlayerMapStats(
      @Parameter(description = "Player ID", example = "1") @PathVariable long playerId) {
    return playerMapStatsService.getPlayerMapStats(playerId);
  }
}
