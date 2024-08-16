package ru.otus.courses.kafka.player.stats.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Entity
@Table(name = "player")
public class Player {

  public Player() {
    this.active = true;
  }

  @Id
  @Column(name = "id")
  private long id;

  @Column(name = "login")
  private String login;

  @Column(name = "email")
  private String email;

  @Column(name = "active")
  private boolean active;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created")
  private ZonedDateTime created;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated")
  private ZonedDateTime updated;
}
