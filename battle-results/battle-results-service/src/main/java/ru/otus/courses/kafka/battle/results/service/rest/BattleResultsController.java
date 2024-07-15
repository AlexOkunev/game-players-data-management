package ru.otus.courses.kafka.battle.results.service.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.courses.kafka.battle.results.service.dto.BattleResultDto;
import ru.otus.courses.kafka.battle.results.service.dto.PageWrappingDTO;
import ru.otus.courses.kafka.battle.results.service.exception.PlayerBattleResultNotFoundException;
import ru.otus.courses.kafka.battle.results.service.service.BattleResultsService;

@RestController
@RequiredArgsConstructor
public class BattleResultsController {

  private final BattleResultsService battleResultsService;

  @GetMapping("/players/{playerId}/battles/results")
  @Operation(summary = "Get player battles results list")
  public PageWrappingDTO<BattleResultDto> getPlayerBattlesResults(
      @Parameter(description = "Player ID", example = "1") @PathVariable long playerId,
      @Parameter(description = "Page number", example = "0") @Min(value = 0) int page,
      @Parameter(description = "Page size", example = "20") @Min(value = 1) int count) {
    return new PageWrappingDTO<>(battleResultsService.getPlayerBattlesResults(playerId, page, count));
  }

  @GetMapping("/players/{playerId}/battles/{battleId}/result")
  @Operation(summary = "Get single player battle result")
  public BattleResultDto getPlayerBattleResult(
      @Parameter(description = "Player ID", example = "1") @PathVariable long playerId,
      @Parameter(description = "Battle ID", example = "1") @PathVariable long battleId) {
    return battleResultsService.getPlayerBattleResult(playerId, battleId)
        .orElseThrow(() -> new PlayerBattleResultNotFoundException(playerId, battleId));
  }

  @GetMapping("/battles/{battleId}/results")
  @Operation(summary = "Get battle results of all battle participants")
  public List<BattleResultDto> getBattleResults(
      @Parameter(description = "Battle ID", example = "1") @PathVariable long battleId) {
    return battleResultsService.getBattleResults(battleId);
  }
}
