package nl.shadowlink.mission.msc.compiler

import nl.shadowlink.mission.msc.scm.CompiledScript

class Compiler {

    private val scriptParser = ScriptParser()

    fun compile(mainSource: String, missionSources: List<String> = emptyList()): CompiledScript {
        val mainScript = scriptParser.parse(mainSource)
        val missionScripts = missionSources.map { scriptParser.parse(it) }

        return CompiledScript(mainScript, missionScripts)
    }
}
