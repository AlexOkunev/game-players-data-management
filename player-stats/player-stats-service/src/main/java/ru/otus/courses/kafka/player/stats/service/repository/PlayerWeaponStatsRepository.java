package ru.otus.courses.kafka.player.stats.service.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.courses.kafka.player.stats.service.entity.PlayerWeaponStats;

public interface PlayerWeaponStatsRepository extends JpaRepository<PlayerWeaponStats, Long> {

  Optional<PlayerWeaponStats> findByPlayerIdAndWeaponId(long playerId, long WeaponId);

  Page<PlayerWeaponStats> findByWeaponId(long weaponId, Pageable pageable);

  List<PlayerWeaponStats> findByPlayerId(long playerId);
}
