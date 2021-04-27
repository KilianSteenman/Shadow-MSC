package nl.shadowlink.mission.msc

import com.xenomachina.argparser.ArgParser
import nl.shadowlink.mission.msc.compiler.Compiler

fun main(args: Array<String>) {
    ArgParser(args).parseInto(::DefaultCompilerArgs).run {
        Compiler().compile(this)
    }
}
