03A4: name_thread 'MISS_1'
0050: gosub @MISSION_MAIN
00D6: if
0112:   wasted_or_busted
004D: jump_if_false @MISSION_END
0050: gosub @MISSION_FAIL

:MISSION_END
0050: gosub @MISSION_CLEANUP
004E: end_thread

:MISSION_MAIN
0317: increment_mission_attempts
0004: $ONMISSION = 1 // $ = int
03E5: text_box 'INTRO'
0247: request_model #PEREN

:MISSION_LOAD_MODEL
0001: wait 0 ms
00D6: if
0248:   model #PEREN available
004D: jump_if_false @MISSION_LOAD_MODEL
00A5: create_car #PEREN at 83.54 -860.669 10.0 0@
016A: fade 5000 1 ms

:MISSION_MAIN_LOOP
0001: wait 0 ms
00D6: if
00DC:   player $PLAYER_CHAR in_car 0@
004D: jump_if_false @MISSION_MAIN_LOOP

:MISSION_PASS
01E3: text_1number_styled 'M_PASS' number 20000 time 5000 style 1  // MISSION PASSED! $~1~
0109: player $PLAYER_CHAR money += 20000
0051: return

:MISSION_FAIL
00BA: text_styled 'M_FAIL' 4000 ms 1  // MISSION FAILED!
0051: return

:MISSION_CLEANUP
0249: release_model #PEREN
01C3: mark_car_as_no_longer_needed 0@
0004: $ONMISSION = 0 // $ = int
00D8: mission_cleanup
0051: return
