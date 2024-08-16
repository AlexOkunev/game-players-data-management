package ru.otus.courses.kafka.battle.results.service.entity;

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
  @Column(name = "id")
  private Long id;

  @NotNull
  @Column(name = "weapon_name")
  private String weaponName;
}