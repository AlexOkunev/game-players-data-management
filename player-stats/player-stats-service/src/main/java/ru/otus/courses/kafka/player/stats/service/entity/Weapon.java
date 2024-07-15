package ru.otus.courses.kafka.player.stats.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "weapon")
public class Weapon {

  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @NotNull
  @Column(name = "weapon_name", nullable = false)
  private String weaponName;

}