package nl.shadowlink.mission.msc.compiler

import nl.shadowlink.mission.msc.CompilerArgs
import nl.shadowlink.mission.msc.binarywriter.FileBinaryWriter
import nl.shadowlink.mission.msc.cleo.CleoLabelOffsetProvider
import nl.shadowlink.mission.msc.scm.CompiledScript
import nl.shadowlink.mission.msc.scm.ScmExporter
import nl.shadowlink.mission.msc.scm.export
import java.io.File

class Compiler {

    private val scriptParser = ScriptParser()

    fun compile(args: CompilerArgs): CompilationState {
        // TODO: write tests for this
        with(args) {
            val mainPath = main
            if (mainPath != null) {
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
                return CompilationState.ScmCompilationSuccess(
                    globalCount = compiledScript.globals.size,
                    missionCount = compiledScript.missionScripts.size,
                    mainSize = compiledScript.mainSizeInBytes,
                    largestMissionSize = compiledScript.largestMissionSizeInBytes,
                    totalScriptSize = compiledScript.totalSize
                )
            }

            val cleoInputSourcePath = cleoInputSource
            if (cleoInputSourcePath != null) {
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
                return CompilationState.CleoCompilationSuccess(scriptSize = script.scriptSizeInBytes)
            }

            return CompilationState.CompilationFailed(error = "Invalid arguments")
        }
    }

    private fun compile(mainSource: String, missionSources: List<String> = emptyList()): CompiledScript {
        val mainScript = scriptParser.parse(mainSource)
        val missionScripts = missionSources.map { scriptParser.parse(it) }

        return CompiledScript(mainScript, missionScripts)
    }
}
