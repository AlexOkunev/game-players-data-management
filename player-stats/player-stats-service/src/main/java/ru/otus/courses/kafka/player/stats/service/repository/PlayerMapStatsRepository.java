package ru.otus.courses.kafka.player.stats.service.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.courses.kafka.player.stats.service.entity.PlayerMapStats;

public interface PlayerMapStatsRepository extends JpaRepository<PlayerMapStats, Long> {

  Optional<PlayerMapStats> findByPlayerIdAndMapName(long playerId, String mapName);

  Page<PlayerMapStats> findByMapName(String mapName, Pageable pageable);

  List<PlayerMapStats> findByPlayerId(long playerId);
}
