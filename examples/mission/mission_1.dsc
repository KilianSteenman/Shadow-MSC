03A4: name_thread 'MISS_1'

:MISSION_LOOP
0001: wait 0 ms
00D6: if 0
80F9:  NOT player $PLAYER_CHAR stopped 89.54 -856.669 10.0 radius 1.0 1.0 2.0 1
004D: jump_if_false @MISSION_PASS
00D6: if 0
80F9:  NOT player $PLAYER_CHAR stopped 89.54 -859.669 10.0 radius 1.0 1.0 2.0 1
004D: jump_if_false @MISSION_FAIL
0002: jump @MISSION_LOOP

:MISSION_PASS
01E3: text_1number_styled 'M_PASS' number 20000 time 5000 style 1  // MISSION PASSED! $~1~
004E: end_thread

:MISSION_FAIL
00BA: text_styled 'M_FAIL' 4000 ms 1  // MISSION FAILED!
004E: end_thread
