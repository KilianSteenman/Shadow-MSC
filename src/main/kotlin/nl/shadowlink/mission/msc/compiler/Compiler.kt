package nl.shadowlink.mission.msc.compiler

class Compiler {

    private val scriptParser = ScriptParser()

    fun compile(mainSource: String, missionSources: List<String> = emptyList()): CompiledScript {
        val mainScript = scriptParser.parse(mainSource)
        val missionScripts = missionSources.map { scriptParser.parse(it) }

        return CompiledScript().apply {
            main = mainScript
            missionScripts.forEach { mission -> addMission(mission) }
        }
    }
}
