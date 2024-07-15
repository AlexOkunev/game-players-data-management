package ru.otus.courses.kafka.players.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import ru.otus.courses.kafka.players.dto.CreatePlayerRequestDto;
import ru.otus.courses.kafka.players.dto.PlayerDto;

public interface PlayersService {

  Optional<PlayerDto> getPlayer(long playerId);

  Page<PlayerDto> getAllPlayers(int page, int size);

  PlayerDto createPlayer(CreatePlayerRequestDto createPlayerRequestDto);
}
