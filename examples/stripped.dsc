03A4: name_thread 'MAIN'
016A: fade 0 0 ms
01F0: set_max_wanted_level_to 6
0111: set_wasted_busted_check_to 0
00C0: set_current_time 12 0
04E4: request_collision_at 468.7745 -1298.623
03CB: load_scene 468.7745 -1298.623 11.0712
0053: create_player 0 at 468.7745 -1298.623 11.0712 $PLAYER_CHAR
01F5: create_emulated_actor_from_player $PLAYER_CHAR $PLAYER_ACTOR
01B6: set_weather 0
0001: wait 0 ms
0180: set_on_mission_flag_to $ONMISSION
00D6: if 0
8118:   not actor $PLAYER_ACTOR dead
004D: jump_if_false @MAIN_138_TEST
0352: set_actor $PLAYER_ACTOR skin_to 'PLAYER'
038B: load_requested_models
0353: refresh_actor $PLAYER_ACTOR

:MAIN_138_TEST
016A: fade 1 1000 ms
00D6: if 0
0256:   player $PLAYER_CHAR defined
004D: jump_if_false @MAIN_174
04BB: select_interiour 0 // select render area
01B4: set_player $PLAYER_CHAR can_move 1
01B7: release_weather

:MAIN_174
0001: wait 250 ms
0002: jump @MAIN_174