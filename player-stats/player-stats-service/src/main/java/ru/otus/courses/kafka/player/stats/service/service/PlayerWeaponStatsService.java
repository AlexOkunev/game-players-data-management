package ru.otus.courses.kafka.player.stats.service.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import ru.otus.courses.kafka.player.stats.service.dto.PlayerWeaponStatsDto;
import ru.otus.courses.kafka.player.stats.service.enumeration.PlayerWeaponStatsSortProperty;

public interface PlayerWeaponStatsService {

  Page<PlayerWeaponStatsDto> getPlayersWeaponStats(long weaponId, int page, int count,
                                                   PlayerWeaponStatsSortProperty sortProperty, Direction direction);

  Optional<PlayerWeaponStatsDto> getPlayerWeaponStats(long playerId, long weaponId);

  List<PlayerWeaponStatsDto> getPlayerWeaponStats(long playerId);
}
