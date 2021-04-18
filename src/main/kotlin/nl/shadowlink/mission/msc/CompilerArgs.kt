package nl.shadowlink.mission.msc

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

class CompilerArgs(parser: ArgParser) {
    val main by parser.storing(
        "--main",
        help = "main source file"
    )

    val cleoInputSource by parser.storing(
        "--ci",
        help = "cleo input source"
    )

    val cleoOuputDir by parser.storing(
        "--co",
        help = "cleo output directory"
    )

    val destination by parser.storing(
        "-o", "--output",
        help = "destination filename"
    ).default<String?>(null)

    val missions by parser.positionalList(
        "MISSIONS",
        help = "Path to missions to include"
    ).default { emptyList() }
}