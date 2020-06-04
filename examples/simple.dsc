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
004D: jump_if_false @MAIN_133
0352: set_actor $PLAYER_ACTOR skin_to 'PLAYER'
038B: load_requested_models
0353: refresh_actor $PLAYER_ACTOR

:MAIN_133
016A: fade 1 1000 ms
00D6: if 0
0256:   player $PLAYER_CHAR defined
004D: jump_if_false @MAIN_704
04BB: select_interiour 0 // select render area
01B4: set_player $PLAYER_CHAR can_move 1
01B7: release_weather
0247: request_model #FAGGIO
0247: request_model #DELUXO
038B: load_requested_models

:MAIN_181
00D6: if 21
8248:   not model #FAGGIO available
8248:   not model #DELUXO available
004D: jump_if_false @MAIN_213
0001: wait 0 ms
0002: jump @MAIN_181

:MAIN_213
00A5: create_car #FAGGIO at 83.54 -851.669 10.0 $6
00A5: create_car #DELUXO at 83.54 -860.669 10.0 $7
0004: $8 = 1 // $ = int
0005: $9 = 304.5 // $ = float
0006: 1@ = 2 // @ = int
0007: 2@ = -403.7998 // @ = float
0008: $8 += 1 // $ += int
0009: $9 += 0.1 // $ += float
0010: $8 *= 2 // $ *= int
0011: $9 *= -1.0 // $ *= float
0247: request_model #CBA
038B: load_requested_models

:MAIN_333
00D6: if 0
8248:   not model #CBA available
004D: jump_if_false @MAIN_359
0001: wait 0 ms
0002: jump @MAIN_333

:MAIN_359
009A: create_actor_pedtype 7 model #CBA at 85.54 -854.669 10.0 4@
0005: $10 = 85.5 // $ = float
0007: 3@ = -854.669 // @ = float
0247: request_model #M4
0247: request_model #UZI
0247: request_model #MP5LNG
038B: load_requested_models

:MAIN_444
00D6: if 22
8248:   not model #UZI available
8248:   not model #M4 available
8248:   not model #MP5LNG available
004D: jump_if_false @MAIN_481
0001: wait 0 ms
0002: jump @MAIN_444

:MAIN_481
01B2: give_actor 4@ weapon 26 ammo 9999 // Load the weapon model before using this
01B1: give_player $PLAYER_CHAR weapon 26 ammo 150 // Load the weapon model before using this
01B8: set_player $PLAYER_CHAR armed_weapon_to 26

:MAIN_508
0001: wait 0 ms
00D6: if 0
00F9:   player $PLAYER_CHAR stopped 87.54 -854.669 10.0 radius 1.0 1.0 2.0 1
004D: jump_if_false @MAIN_508
0211: actor 4@ walk_to 75.0 -854.669

:MAIN_575
0001: wait 0 ms
00D6: if 0
0102:   actor 4@ stopped_near_point_on_foot 75.0 -854.669 10.0 radius 1.0 1.0 2.0 sphere 1
004D: jump_if_false @MAIN_575
0239: actor 4@ run_to 60.0 -854.669

:MAIN_642
0001: wait 0 ms
00D6: if 0
0102:   actor 4@ stopped_near_point_on_foot 60.0 -854.669 10.0 radius 1.0 1.0 2.0 sphere 1
004D: jump_if_false @MAIN_642
0005: $11 = 1.0 // $ = float

:MAIN_704
0001: wait 50 ms
0173: set_actor 4@ z_angle_to $11
0009: $11 += 0.1 // $ += float
0002: jump @MAIN_704