package nl.shadowlink.mission.msc

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

class CompilerArgs(parser: ArgParser) {
    val source by parser.positional(
        "SOURCE",
        help = "source filename")

    val destination by parser.positional(
        "DEST",
        help = "destination filename").default<String?>(null)
}