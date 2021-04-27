package nl.shadowlink.mission.msc.scm

import nl.shadowlink.mission.msc.compiler.LabelOffsetProvider
import nl.shadowlink.mission.msc.compiler.Script

class CompiledScript(
    val mainScript: Script,
    val missionScripts: List<Script> = emptyList()
) : LabelOffsetProvider {

    val totalSize: Int
        get() = headerSize + mainSizeInBytes + missionScripts.sumBy { it.scriptSizeInBytes }

    private val _globals = mutableListOf<String>()
    val globals: List<String> = _globals

    private val _objects = mutableListOf<String>()
    val objects: List<String> = _objects

    val headerSize: Int
        get() = 64 + (objects.size * 24) + (globals.size * 4) + (missionScripts.size * 4)

    val mainSizeInBytes: Int
        get() = mainScript.scriptSizeInBytes

    val largestMissionSizeInBytes: Int
        get() = missionScripts.sortedWith(compareBy(Script::scriptSizeInBytes)).lastOrNull()?.scriptSizeInBytes ?: 0

    init {
        mainScript.isMainScript = true
        addObjectsAndGlobals(mainScript)

        missionScripts.forEach { missionScript -> addObjectsAndGlobals(missionScript) }
    }

    private fun addObjectsAndGlobals(script: Script) {
        addObjectsFromScript(script)
        addGlobalsFromScript(script)
    }

    private fun addObjectsFromScript(script: Script) {
        script.objects.forEach { objectName ->
            if (!_objects.contains(objectName)) {
                _objects.add(objectName)
            }
        }
    }

    private fun addGlobalsFromScript(script: Script) {
        script.globals.forEach { global ->
            if (!_globals.contains(global)) {
                _globals.add(global)
            }
        }
    }

    fun getOffsetForMission(index: Int): Int {
        return headerSize + mainSizeInBytes + missionScripts.subList(0, index).sumBy { it.scriptSizeInBytes }
    }

    override val labelOffset: Int
        get() = headerSize
}