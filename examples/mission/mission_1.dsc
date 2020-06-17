03A4: name_thread 'MISS_1'
016A: fade 0 0 ms
01B4: set_player $PLAYER_CHAR can_move 0
00C0: set_current_time 2 0
01B6: set_weather 1

:MISSION_START
016A: fade 1 1000 ms
01B4: set_player $PLAYER_CHAR can_move 1
01B7: release_weather
0247: request_model #DELUXO
0247: request_model #CBA
038B: load_requested_models

:LOAD_MODELS
00D6: if 21
8248:   not model #DELUXO available
8248:   not model #CBA available
004D: jump_if_false @MODELS_LOADED
0001: wait 0 ms
0002: jump @LOAD_MODELS

:MODELS_LOADED
00A5: create_car #DELUXO at 83.54 -860.669 10.0 $VEHICLE

:CHECK_PLAYER_IN_CAR
00D6: if 0
8448:  NOT actor $PLAYER_ACTOR in_car $VEHICLE
004D: jump_if_false @PLAYER_IN_VEHICLE
0001: wait 0 ms
0002: jump @CHECK_PLAYER_IN_CAR

:PLAYER_IN_VEHICLE
0394: play_music  1
01E3: text_1number_styled "M_PASS"  200  5000 ms  1  ;; MISSION PASSED! $~1~
0110: clear_player $PLAYER_CHAR wanted_level
0109: player $PLAYER_CHAR money +=  200
00D8: mission_cleanup
0051: return
