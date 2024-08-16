package ru.otus.courses.kafka.player.stats.service.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import ru.otus.courses.kafka.player.stats.service.dto.PlayerWeaponStatsDto;
import ru.otus.courses.kafka.player.stats.service.entity.PlayerWeaponStats;
import ru.otus.courses.kafka.player.stats.service.enumeration.PlayerWeaponStatsSortProperty;
import ru.otus.courses.kafka.player.stats.service.repository.PlayerWeaponStatsRepository;
import ru.otus.courses.kafka.player.stats.service.service.PlayerWeaponStatsService;

@Service
@RequiredArgsConstructor
public class PlayerWeaponStatsServiceImpl implements PlayerWeaponStatsService {

  private final PlayerWeaponStatsRepository playerWeaponStatsRepository;

  @Override
  public Page<PlayerWeaponStatsDto> getPlayersWeaponStats(long weaponId, int page, int count,
                                                          PlayerWeaponStatsSortProperty sortProperty,
                                                          Direction direction) {
    PageRequest pageRequest = PageRequest.of(page, count, Sort.by(direction, sortProperty.getFieldName()));
    return playerWeaponStatsRepository.findByWeaponId(weaponId, pageRequest)
        .map(this::toDto);
  }

  @Override
  public Optional<PlayerWeaponStatsDto> getPlayerWeaponStats(long playerId, long weaponId) {
    return playerWeaponStatsRepository.findByPlayerIdAndWeaponId(playerId, weaponId)
        .map(this::toDto);
  }

  @Override
  public List<PlayerWeaponStatsDto> getPlayerWeaponStats(long playerId) {
    return playerWeaponStatsRepository.findByPlayerId(playerId).stream()
        .map(this::toDto)
        .toList();
  }

  private PlayerWeaponStatsDto toDto(PlayerWeaponStats playerWeaponStats) {
    return PlayerWeaponStatsDto.builder()
        .playerId(playerWeaponStats.getPlayer().getId())
        .login(playerWeaponStats.getPlayer().getLogin())
        .weaponId(playerWeaponStats.getWeapon().getId())
        .weaponName(playerWeaponStats.getWeapon().getWeaponName())
        .battlesCount(playerWeaponStats.getBattlesCount())
        .shotsCount(playerWeaponStats.getShotsCount())
        .successfulShotsCount(playerWeaponStats.getSuccessfulShotsCount())
        .killsCount(playerWeaponStats.getKillsCount())
        .headshotsCount(playerWeaponStats.getHeadshotsCount())
        .damageSum(playerWeaponStats.getDamageSum())
        .successfulShotsRate(playerWeaponStats.getSuccessfulShotsRate())
        .avgSuccessfulShotDamage(playerWeaponStats.getAvgSuccessfulShotDamage())
        .headshotsToSuccessfulShotsRate(playerWeaponStats.getHeadshotsToSuccessfulShotsRate())
        .build();
  }
}
