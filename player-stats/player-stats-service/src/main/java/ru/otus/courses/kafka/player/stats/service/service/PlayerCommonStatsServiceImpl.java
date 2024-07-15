package ru.otus.courses.kafka.player.stats.service.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import ru.otus.courses.kafka.player.stats.service.dto.PlayerCommonStatsDto;
import ru.otus.courses.kafka.player.stats.service.entity.PlayerCommonStats;
import ru.otus.courses.kafka.player.stats.service.enumeration.SortProperty;
import ru.otus.courses.kafka.player.stats.service.repository.PlayerCommonStatsRepository;

@Service
@RequiredArgsConstructor
public class PlayerCommonStatsServiceImpl implements PlayerCommonStatsService {

  private final PlayerCommonStatsRepository playerCommonStatsRepository;

  @Override
  public Page<PlayerCommonStatsDto> getPlayersCommonStats(int page, int count, SortProperty sortProperty,
                                                          Direction direction) {
    PageRequest pageRequest = PageRequest.of(page, count, Sort.by(direction, sortProperty.getFieldName()));
    return playerCommonStatsRepository.findAll(pageRequest)
        .map(this::toDto);
  }

  @Override
  public Optional<PlayerCommonStatsDto> getPlayerCommonStats(long playerId) {
    return playerCommonStatsRepository.findById(playerId)
        .map(this::toDto);
  }

  private PlayerCommonStatsDto toDto(PlayerCommonStats playerCommonStats) {
    return PlayerCommonStatsDto.builder()
        .playerId(playerCommonStats.getPlayerId())
        .login(playerCommonStats.getPlayer().getLogin())
        .battlesCount(playerCommonStats.getBattlesCount())
        .shotsCount(playerCommonStats.getShotsCount())
        .successfulShotsCount(playerCommonStats.getSuccessfulShotsCount())
        .killsCount(playerCommonStats.getKillsCount())
        .headshotsCount(playerCommonStats.getHeadshotsCount())
        .deathsCount(playerCommonStats.getDeathsCount())
        .winsCount(playerCommonStats.getWinsCount())
        .damageSum(playerCommonStats.getDamageSum())
        .winsRate(playerCommonStats.getWinsRate())
        .successfulShotsRate(playerCommonStats.getSuccessfulShotsRate())
        .avgSuccessfulShotDamage(playerCommonStats.getAvgSuccessfulShotDamage())
        .headshotsToSuccessfulShotsRate(playerCommonStats.getHeadshotsToSuccessfulShotsRate())
        .build();
  }
}
