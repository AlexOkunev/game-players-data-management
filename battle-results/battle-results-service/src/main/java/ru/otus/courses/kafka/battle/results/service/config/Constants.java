package ru.otus.courses.kafka.battle.results.service.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

  @UtilityClass
  public static class Topics {

    public static final String BATTLE_EVENTS = "battle-events";
    public static final String PLAYER_BATTLE_RESULTS = "player-battle-results";
    public static final String PLAYER_BATTLE_RESULTS_DB = "db.player-battle-results";
  }

  @UtilityClass
  public static class StateStores {

    public static final String BATTLE_RESULTS_STATE_STORE = "battle-results-store";
    public static final String BATTLE_INFO_STATE_STORE = "battle-info-store";
    public static final String BATTLES_TO_SEND_STATE_STORE = "battles-to-send-store";
  }
}
