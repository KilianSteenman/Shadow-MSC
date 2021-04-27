package nl.shadowlink.mission.msc

import com.xenomachina.argparser.ArgParser
import nl.shadowlink.mission.msc.binarywriter.FileBinaryWriter
import nl.shadowlink.mission.msc.cleo.CleoLabelOffsetProvider
import nl.shadowlink.mission.msc.compiler.Compiler
import nl.shadowlink.mission.msc.compiler.ScriptParser
import nl.shadowlink.mission.msc.scm.ScmExporter
import nl.shadowlink.mission.msc.scm.export
import java.io.File

fun main(args: Array<String>) {
    ArgParser(args).parseInto(::DefaultCompilerArgs).run {
        startCompilation(this)
    }
}

fun startCompilation(args: CompilerArgs) {
    // TODO: write tests for this
    with(args) {
        val mainPath = main
        if(mainPath != null) {
            val mainSrc = File(mainPath).readText()
            val missionSrc = missions.map { mission -> File(mission).readText() }
            val compiledScript = Compiler().compile(mainSrc, missionSrc)
            val destination = destination ?: mainPath.replaceAfterLast(".", "scm")

            // Make sure we have a clean file to work with
            with(File(destination)) {
                if (exists()) {
                    delete()
                }
            }

            println("Compiling $mainPath to $destination")
            ScmExporter().export(FileBinaryWriter(destination), compiledScript)
            println(
                "Compilation finished\n" +
                        "Globals: ${compiledScript.globals.size}\n" +
                        "Missions: ${compiledScript.missionScripts.size}\n" +
                        "Total script size: ${compiledScript.totalSize} bytes\n" +
                        "Main size: ${compiledScript.mainSizeInBytes} bytes\n" +
                        "Largest mission size: ${compiledScript.largestMissionSizeInBytes} bytes\n"
            )
        }

        val cleoInputSourcePath = cleoInputSource
        if(cleoInputSourcePath != null) {
            val cleoDestination = cleoOutputPath ?: cleoInputSourcePath.replaceAfterLast(".", "cs")

            // Make sure we have a clean file to work with
            with(File(cleoDestination)) {
                if (exists()) {
                    delete()
                }
            }

            println("Compiling cleo $cleoInputSourcePath to $cleoDestination")
            val script = ScriptParser().parse(File(cleoInputSourcePath).readText())
            script.export(FileBinaryWriter(cleoDestination), CleoLabelOffsetProvider)
            println("Cleo compilation finished")
        }
    }
}