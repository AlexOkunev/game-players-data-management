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
import ru.otus.courses.kafka.player.stats.service.dto.PlayerWeaponStatsDto;
import ru.otus.courses.kafka.player.stats.service.enumeration.PlayerWeaponStatsSortProperty;
import ru.otus.courses.kafka.player.stats.service.exception.PlayerStatsNotFoundException;
import ru.otus.courses.kafka.player.stats.service.service.PlayerWeaponStatsService;

@RestController
@RequiredArgsConstructor
public class PlayersWeaponStatsController {

  private final PlayerWeaponStatsService playerWeaponStatsService;

  @GetMapping("/players/stats/weapon/{weaponId}")
  @Operation(summary = "Get players weapon stats")
  public PageWrappingDTO<PlayerWeaponStatsDto> getPlayersWeaponStats(
      @Parameter(description = "Page number", example = "0") @Min(value = 0) int page,
      @Parameter(description = "Page size", example = "20") @Min(value = 1) int count,
      @Parameter PlayerWeaponStatsSortProperty sortProperty, @Parameter Direction direction,
      @Parameter(description = "Weapon ID", example = "1") @PathVariable long weaponId) {
    return new PageWrappingDTO<>(
        playerWeaponStatsService.getPlayersWeaponStats(weaponId, page, count, sortProperty, direction));
  }

  @GetMapping("/players/{playerId}/stats/weapon/{weaponId}")
  @Operation(summary = "Get single player weapon stats for single weapon")
  public PlayerWeaponStatsDto getPlayerWeaponStats(
      @Parameter(description = "Player ID", example = "1") @PathVariable long playerId,
      @Parameter(description = "Weapon ID", example = "1") @PathVariable long weaponId) {
    return playerWeaponStatsService.getPlayerWeaponStats(playerId, weaponId)
        .orElseThrow(() -> new PlayerStatsNotFoundException(playerId));
  }

  @GetMapping("/players/{playerId}/stats/weapon")
  @Operation(summary = "Get single player weapon stats for all weapons")
  public List<PlayerWeaponStatsDto> getPlayerWeaponStats(
      @Parameter(description = "Player ID", example = "1") @PathVariable long playerId) {
    return playerWeaponStatsService.getPlayerWeaponStats(playerId);
  }
}
