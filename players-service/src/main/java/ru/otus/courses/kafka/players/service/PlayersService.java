package ru.otus.courses.kafka.players.service;

import java.util.List;
import java.util.Optional;
import ru.otus.courses.kafka.players.dto.CreatePlayerDto;
import ru.otus.courses.kafka.players.dto.PlayerDto;

public interface PlayersService {

  Optional<PlayerDto> getPlayer(long playerId);

  List<PlayerDto> getAllPlayers(int page, int size);

  PlayerDto createPlayer(CreatePlayerDto createPlayerRequestDto);
}
