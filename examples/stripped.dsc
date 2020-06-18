03A4: name_thread 'MAIN'
016A: fade 0 0 ms
01F0: set_max_wanted_level_to 6
0111: set_wasted_busted_check_to 0
00C0: set_current_time 12 0
04E4: request_collision_at 83.0 -849.8
03CB: load_scene 83.0 -849.8 9.3
0053: create_player 0 at 80.0 -849.8 9.3 $PLAYER_CHAR
01F5: create_emulated_actor_from_player $PLAYER_CHAR $PLAYER_ACTOR
0001: wait 0 ms
01B6: set_weather 0
00D6: if 0
8118:   not actor $PLAYER_ACTOR dead
004D: jump_if_false @MAIN_2
0352: set_actor $PLAYER_ACTOR skin_to 'PLAYER'
038B: load_requested_models
0353: refresh_actor $PLAYER_ACTOR

:MAIN_2
016A: fade 1 1000 ms
00D6: if 0
0256:   player $PLAYER_CHAR defined
004D: jump_if_false @MAIN_3
04BB: select_interiour 0 // select render area
01B4: set_player $PLAYER_CHAR can_move 1
01B7: release_weather

:MAIN_3
0001: wait 2500 ms
0002: jump @MAIN_3