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

        // Make sure we have a clean file to work with
        with(File(destination)) {
            if (exists()) {
                delete()
            }
        }

        println("Compiling $main to $destination")
        ScmExporter().export(FileBinaryWriter(destination), script)
        println(
            "Compilation finished\n" +
                    "Globals: ${script.globals.size}\n" +
                    "Missions: ${script.missionScripts.size}\n" +
                    "Total script size: ${script.totalSize} bytes\n" +
                    "Main size: ${script.mainSizeInBytes} bytes\n" +
                    "Largest mission size: ${script.largestMissionSizeInBytes} bytes\n"
        )
    }
}