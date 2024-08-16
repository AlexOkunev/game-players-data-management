package ru.otus.courses.kafka.player.stats.service.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import ru.otus.courses.kafka.player.stats.service.dto.PlayerMapStatsDto;
import ru.otus.courses.kafka.player.stats.service.enumeration.PlayerMapStatsSortProperty;

public interface PlayerMapStatsService {

  Page<PlayerMapStatsDto> getPlayersMapStats(String mapName, int page, int count,
                                             PlayerMapStatsSortProperty sortProperty, Direction direction);

  Optional<PlayerMapStatsDto> getPlayerMapStats(long playerId, String mapName);

  List<PlayerMapStatsDto> getPlayerMapStats(long playerId);
}
