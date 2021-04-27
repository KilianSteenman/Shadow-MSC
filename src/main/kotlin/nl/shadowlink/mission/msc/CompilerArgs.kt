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

internal class DefaultCompilerArgs(parser: ArgParser) : CompilerArgs {
    override val main by parser.storing(
        "--main",
        help = "main source file"
    ).default<String?>(null)

    override val destination by parser.storing(
        "-o", "--output",
        help = "destination filename"
    ).default<String?>(null)

    override val missions by parser.positionalList(
        "MISSIONS",
        help = "Path to missions to include"
    ).default { emptyList() }

    override val cleoInputSource by parser.storing(
        "--cleo-input",
        help = "cleo input source"
    ).default<String?>(null)

    override val cleoOutputPath by parser.storing(
        "--cleo-output",
        help = "cleo output directory"
    ).default<String?>(null)
}