package ru.otus.courses.kafka.gameserver.model;

public record PlayerShot(long shooterPlayerId, Long victimPlayerId, long weaponId, int damage, boolean headshot,
                         boolean killed, int delaySeconds) {

}
