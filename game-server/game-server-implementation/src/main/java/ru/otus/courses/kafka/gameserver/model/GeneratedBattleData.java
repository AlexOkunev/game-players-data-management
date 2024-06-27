package ru.otus.courses.kafka.gameserver.model;

import java.util.List;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleConnectionEvent;
import ru.otus.courses.kafka.game.server.datatypes.events.BattleEvent;

public record GeneratedBattleData(List<BattleEvent> battleEvents, List<BattleEvent> delayedBattleEvents,
                                  List<BattleConnectionEvent> battleConnectionEvents) {

}
