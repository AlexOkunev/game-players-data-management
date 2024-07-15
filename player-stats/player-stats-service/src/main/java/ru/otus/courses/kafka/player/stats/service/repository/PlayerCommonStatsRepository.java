package ru.otus.courses.kafka.player.stats.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.courses.kafka.player.stats.service.entity.PlayerCommonStats;

public interface PlayerCommonStatsRepository extends JpaRepository<PlayerCommonStats, Long> {

}
