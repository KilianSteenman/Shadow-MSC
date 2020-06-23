package nl.shadowlink.mission.msc

import com.xenomachina.argparser.ArgParser
import nl.shadowlink.mission.msc.binarywriter.FileBinaryWriter
import nl.shadowlink.mission.msc.compiler.CompiledScript
import nl.shadowlink.mission.msc.compiler.Compiler
import nl.shadowlink.mission.msc.compiler.ScmExporter
import java.io.File

fun main(args: Array<String>) {
    ArgParser(args).parseInto(::CompilerArgs).run {
        val script = when {
            !main.isNullOrBlank() -> compileScm(main!!, missions)
            !cleo.isNullOrBlank() -> compileCleo(cleo!!)
            else -> throw IllegalArgumentException("Missing required main or cleo parameter")
        }

        val destination = destination
            ?: main?.replaceAfterLast(".", "scm")
            ?: cleo?.replaceAfterLast(".", ".cs")
            ?: throw IllegalStateException("Unknown destination")

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
                    "Missions: ${script.missions.size}\n" +
                    "Total script size: ${script.totalSize} bytes\n" +
                    "Main size: ${script.mainSizeInBytes} bytes\n" +
                    "Largest mission size: ${script.largestMissionSizeInBytes} bytes\n"
        )
    }
}

fun compileScm(mainFile: String, missionFiles: List<String>): CompiledScript {
    val mainSrc = File(mainFile).readText()
    val missionSrc = missionFiles.map { mission -> File(mission).readText() }
    return Compiler().compile(mainSrc, missionSrc)
}

fun compileCleo(cleoFile: String): CompiledScript {
    val cleoSrc = File(cleoFile).readText()
    return Compiler().compile(cleoSrc)
}