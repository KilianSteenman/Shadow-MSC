package nl.shadowlink.mission.msc

import com.xenomachina.argparser.ArgParser
import nl.shadowlink.mission.msc.binarywriter.FileBinaryWriter

fun main(args: Array<String>) {
    ArgParser(args).parseInto(::CompilerArgs).run {
        val script = Compiler().compile(source)
        val destination = destination ?: source.replaceAfterLast(".", "scm")

        println("Compiling $source to $destination")
        ScmExporter().export(FileBinaryWriter(destination), script)
    }
}