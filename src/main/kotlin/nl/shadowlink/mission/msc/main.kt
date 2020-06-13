package nl.shadowlink.mission.msc

import com.xenomachina.argparser.ArgParser
import nl.shadowlink.mission.msc.binarywriter.FileBinaryWriter
import nl.shadowlink.mission.msc.compiler.Compiler
import nl.shadowlink.mission.msc.compiler.ScmExporter
import java.io.File

fun main(args: Array<String>) {
    ArgParser(args).parseInto(::CompilerArgs).run {
        val mainSrc = File(main).readText()
        val missionSrc = missions.map { mission -> File(mission).readText() }
        val script = Compiler().compile(mainSrc, missionSrc)
        val destination = destination ?: main.replaceAfterLast(".", "scm")

        println("Compiling $main to $destination")
        ScmExporter().export(FileBinaryWriter(destination), script)
        println(
            "Compilation finished\n" +
                    "Globals: ${script.globals.size}\n" +
                    "Missions: ${script.missions.size}\n" +
                    "Main size: ${script.scriptSizeInBytes} bytes\n" +
                    "Largest mission size: ${script.largestMissionSizeInBytes} bytes\n"
        )
    }
}