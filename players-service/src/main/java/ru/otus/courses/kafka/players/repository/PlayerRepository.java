package ru.otus.courses.kafka.players.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.courses.kafka.players.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

}
