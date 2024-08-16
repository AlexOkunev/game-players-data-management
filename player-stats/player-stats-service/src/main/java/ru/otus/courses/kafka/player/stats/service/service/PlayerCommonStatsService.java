package ru.otus.courses.kafka.player.stats.service.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import ru.otus.courses.kafka.player.stats.service.dto.PlayerCommonStatsDto;
import ru.otus.courses.kafka.player.stats.service.enumeration.PlayerCommonStatsSortProperty;

public interface PlayerCommonStatsService {

  Page<PlayerCommonStatsDto> getPlayersCommonStats(int page, int count, PlayerCommonStatsSortProperty sortProperty, Direction direction);

  Optional<PlayerCommonStatsDto> getPlayerCommonStats(long playerId);
}
