package ru.otus.courses.kafka.battle.results.service.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import ru.otus.courses.kafka.battle.results.service.dto.BattleResultDto;

public interface BattleResultsService {

  Page<BattleResultDto> getPlayerBattlesResults(long playerId, int page, int count);

  Optional<BattleResultDto> getPlayerBattleResult(long playerId, long battleId);

  List<BattleResultDto> getBattleResults(long battleId);
}
