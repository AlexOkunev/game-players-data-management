package ru.otus.courses.kafka.player.stats.service.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import ru.otus.courses.kafka.player.stats.service.dto.PlayerMapStatsDto;
import ru.otus.courses.kafka.player.stats.service.entity.PlayerMapStats;
import ru.otus.courses.kafka.player.stats.service.enumeration.PlayerMapStatsSortProperty;
import ru.otus.courses.kafka.player.stats.service.repository.PlayerMapStatsRepository;
import ru.otus.courses.kafka.player.stats.service.service.PlayerMapStatsService;

@Service
@RequiredArgsConstructor
public class PlayerMapStatsServiceImpl implements PlayerMapStatsService {

  private final PlayerMapStatsRepository playerMapStatsRepository;

  @Override
  public Page<PlayerMapStatsDto> getPlayersMapStats(String mapName, int page, int count,
                                                    PlayerMapStatsSortProperty sortProperty, Direction direction) {
    PageRequest pageRequest = PageRequest.of(page, count, Sort.by(direction, sortProperty.getFieldName()));
    return playerMapStatsRepository.findByMapName(mapName, pageRequest)
        .map(this::toDto);
  }

  @Override
  public Optional<PlayerMapStatsDto> getPlayerMapStats(long playerId, String mapName) {
    return playerMapStatsRepository.findByPlayerIdAndMapName(playerId, mapName)
        .map(this::toDto);
  }

  @Override
  public List<PlayerMapStatsDto> getPlayerMapStats(long playerId) {
    return playerMapStatsRepository.findByPlayerId(playerId).stream()
        .map(this::toDto)
        .toList();
  }

  private PlayerMapStatsDto toDto(PlayerMapStats playerMapStats) {
    return PlayerMapStatsDto.builder()
        .playerId(playerMapStats.getPlayer().getId())
        .mapName(playerMapStats.getMapName())
        .login(playerMapStats.getPlayer().getLogin())
        .battlesCount(playerMapStats.getBattlesCount())
        .shotsCount(playerMapStats.getShotsCount())
        .successfulShotsCount(playerMapStats.getSuccessfulShotsCount())
        .killsCount(playerMapStats.getKillsCount())
        .headshotsCount(playerMapStats.getHeadshotsCount())
        .deathsCount(playerMapStats.getDeathsCount())
        .winsCount(playerMapStats.getWinsCount())
        .damageSum(playerMapStats.getDamageSum())
        .winsRate(playerMapStats.getWinsRate())
        .successfulShotsRate(playerMapStats.getSuccessfulShotsRate())
        .avgSuccessfulShotDamage(playerMapStats.getAvgSuccessfulShotDamage())
        .headshotsToSuccessfulShotsRate(playerMapStats.getHeadshotsToSuccessfulShotsRate())
        .build();
  }
}
