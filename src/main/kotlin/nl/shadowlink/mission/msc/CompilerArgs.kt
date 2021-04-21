package nl.shadowlink.mission.msc

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

interface CompilerArgs {
    val main: String?

    val destination: String?

    val missions: List<String>

    val cleoInputSource: String?

    val cleoOutputPath: String?
}

class DefaultCompilerArgs(parser: ArgParser): CompilerArgs {
    override val main by parser.storing(
        "--main",
        help = "main source file"
    )

    override val destination by parser.storing(
        "-o", "--output",
        help = "destination filename"
    ).default<String?>(null)

    override val missions by parser.positionalList(
        "MISSIONS",
        help = "Path to missions to include"
    ).default { emptyList() }

    override val cleoInputSource by parser.storing(
        "--ci",
        help = "cleo input source"
    )

    override val cleoOutputPath by parser.storing(
        "--co",
        help = "cleo output directory"
    )
}