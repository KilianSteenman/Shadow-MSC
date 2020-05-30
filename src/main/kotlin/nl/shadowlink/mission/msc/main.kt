package nl.shadowlink.mission.msc

import nl.shadowlink.mission.msc.binarywriter.FileBinaryWriter

fun main(args: Array<String>) {
    val script = Compiler().compile(TEST_SCRIPT_2)
    ScmExporter().export(FileBinaryWriter("/Users/kilian/test.scm"), script)
}

private val TEST_SCRIPT_1 = ":Label\n" +
        "03A4: name_thread 'MAIN'\n" +
        "0001: wait\n" +
        "04E4: request_collision_at 83.0 -849.8\n" +
        ":Label\n" +
        "0001: wait\n" +
        "0002: jump @Label\n"

private val TEST_SCRIPT_2 = "03A4: name_thread 'MAIN'\n" +
        "016A: fade 0 0 ms\n" +
        "01F0: set_max_wanted_level_to 6\n" +
        "0111: set_wasted_busted_check_to 0\n" +
        "00C0: set_current_time 12 0\n" +
        "04E4: request_collision_at 468.7745 -1298.623\n" +
        "03CB: load_scene 468.7745 -1298.623 11.0712\n" +
        "0053: \$PLAYER_CHAR = create_player #NULL at 468.7745 -1298.623 11.0712\n" +
        "01F5: \$PLAYER_ACTOR = create_emulated_actor_from_player \$PLAYER_CHAR\n" +
        "01B6: set_weather 0\n" +
        "0001: wait 0 ms\n" +
        "0180: set_on_mission_flag_to \$ONMISSION\n" +
        "00D6: if\n" +
        "8118:   not actor \$PLAYER_ACTOR dead\n" +
        "004D: jump_if_false @MAIN_138_TEST\n" +
        "0352: set_actor \$PLAYER_ACTOR skin_to 'PLAYER'\n" +
        "038B: load_requested_models\n" +
        "0353: refresh_actor \$PLAYER_ACTOR\n" +
        "\n" +
        ":MAIN_138_TEST\n" +
        "016A: fade 1 1000 ms\n" +
        "00D6: if\n" +
        "0256:   player \$PLAYER_CHAR defined\n" +
        "004D: jump_if_false @MAIN_174\n" +
        "04BB: select_interiour 0 // select render area\n" +
        "01B4: set_player \$PLAYER_CHAR can_move 1\n" +
        "01B7: release_weather\n" +
        "\n" +
        ":MAIN_174\n" +
        "0001: wait 250 ms\n" +
        "0002: jump @MAIN_174\n"