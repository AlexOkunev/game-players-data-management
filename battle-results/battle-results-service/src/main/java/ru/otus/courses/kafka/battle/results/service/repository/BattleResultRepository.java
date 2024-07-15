package ru.otus.courses.kafka.battle.results.service.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.courses.kafka.battle.results.service.entity.PlayerBattleResult;

public interface BattleResultRepository extends JpaRepository<PlayerBattleResult, Long> {

  Page<PlayerBattleResult> findAllByPlayerId(long playerId, Pageable pageable);

  List<PlayerBattleResult> findAllByIdBattleId(long battleId);

  Optional<PlayerBattleResult> findByPlayerIdAndIdBattleId(long playerId, long battleId);
}
