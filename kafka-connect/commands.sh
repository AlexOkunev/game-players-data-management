curl -X POST --data-binary "@./connectors/players_source.json" -H "Content-Type: application/json" http://localhost:8083/connectors | jq

curl -X POST --data-binary "@./connectors/player_common_stats_sink.json" -H "Content-Type: application/json" http://localhost:8083/connectors | jq

curl -X POST --data-binary "@./connectors/player_weapon_stats_sink.json" -H "Content-Type: application/json" http://localhost:8083/connectors | jq

curl -X POST --data-binary "@./connectors/player_map_stats_sink.json" -H "Content-Type: application/json" http://localhost:8083/connectors | jq