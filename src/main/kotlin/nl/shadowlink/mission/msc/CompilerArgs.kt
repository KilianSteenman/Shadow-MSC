package nl.shadowlink.mission.msc

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

class CompilerArgs(parser: ArgParser) {
    val main by parser.storing(
        "--main",
        help = "main source file"
    ).default<String?>(null)

    val cleo by parser.storing(
        "--cleo",
        help = "cleo source file"
    ).default<String?>(null)

    val destination by parser.storing(
        "-o", "--output",
        help = "destination filename"
    ).default<String?>(null)

    val missions by parser.positionalList(
        "MISSIONS",
        help = "Path to missions to include"
    ).default { emptyList() }
}