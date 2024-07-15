package ru.otus.courses.kafka.battle.results.service.service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.otus.courses.kafka.battle.results.service.dto.BattleResultDto;
import ru.otus.courses.kafka.battle.results.service.entity.PlayerBattleResult;
import ru.otus.courses.kafka.battle.results.service.repository.BattleResultRepository;

@Service
@RequiredArgsConstructor
public class BattleResultsServiceImpl implements BattleResultsService {

  private final BattleResultRepository battleResultRepository;

  @Override
  public Page<BattleResultDto> getPlayerBattlesResults(long playerId, int page, int count) {
    return battleResultRepository.findAllByPlayerId(playerId, PageRequest.of(page, count))
        .map(this::toDto);
  }

  @Override
  public Optional<BattleResultDto> getPlayerBattleResult(long playerId, long battleId) {
    return battleResultRepository.findByPlayerIdAndIdBattleId(playerId, battleId)
        .map(this::toDto);
  }

  @Override
  public List<BattleResultDto> getBattleResults(long battleId) {
    return battleResultRepository.findAllByIdBattleId(battleId).stream()
        .map(this::toDto)
        .toList();
  }

  private BattleResultDto toDto(PlayerBattleResult playerBattleResult) {
    return BattleResultDto.builder()
        .playerId(playerBattleResult.getPlayer().getId())
        .login(playerBattleResult.getPlayer().getLogin())
        .battleId(playerBattleResult.getId().getBattleId())
        .battleMap(playerBattleResult.getMap())
        .battleFinishedTime(
            Instant.ofEpochMilli(playerBattleResult.getBattleFinishedTimestamp())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime())
        .shotsCount(playerBattleResult.getShots())
        .successfulShotsCount(playerBattleResult.getSuccessfulShots())
        .killsCount(playerBattleResult.getKilled())
        .headshotsCount(playerBattleResult.getHeadshots())
        .deathsCount(playerBattleResult.getDeaths())
        .winner(playerBattleResult.getWinner())
        .damageSum(playerBattleResult.getDamageSum())
        .build();
  }
}
