package ru.otus.courses.kafka.players.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.otus.courses.kafka.players.dto.CreatePlayerDto;
import ru.otus.courses.kafka.players.dto.PlayerDto;
import ru.otus.courses.kafka.players.entity.Player;
import ru.otus.courses.kafka.players.repository.PlayerRepository;
import ru.otus.courses.kafka.players.service.PlayersService;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayersService {

  private final PlayerRepository playerRepository;

  @Override
  public Optional<PlayerDto> getPlayer(long playerId) {
    return playerRepository.findById(playerId)
        .map(this::toDto);
  }

  @Override
  public List<PlayerDto> getAllPlayers(int page, int size) {
    return playerRepository.findAll(PageRequest.of(page, size)).stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  public PlayerDto createPlayer(CreatePlayerDto createPlayerRequestDto) {
    Player player = new Player();
    player.setLogin(createPlayerRequestDto.getLogin());
    player.setEmail(createPlayerRequestDto.getEmail());

    player = playerRepository.save(player);

    return toDto(player);
  }

  private PlayerDto toDto(Player player) {
    PlayerDto playerDto = new PlayerDto();
    playerDto.setId(player.getId());
    playerDto.setLogin(player.getLogin());
    playerDto.setEmail(player.getEmail());
    playerDto.setActive(player.isActive());
    playerDto.setCreated(player.getCreated());
    playerDto.setUpdated(player.getUpdated());
    return playerDto;
  }
}
