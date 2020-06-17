package nl.shadowlink.mission.msc.compiler

class CompiledScript {

    val totalSize: Int
        get() = headerSize + mainSizeInBytes + _missions.sumBy { it.scriptSizeInBytes }

    private val _globals = mutableListOf<String>()
    val globals: List<String> = _globals

    private val _objects = mutableListOf<String>()
    val objects: List<String> = _objects

    var main: Script? = null
        set(mainScript) {
            field = mainScript
            mainScript?.let {
                addObjectsFromScript(it)
                addGlobalsFromScript(it)
                it.isMainScript = true
            }
        }

    private val _missions = mutableListOf<Script>()
    val missions: List<Script> = _missions

    val headerSize: Int
        get() = 64 + (objects.size * 24) + (globals.size * 4) + (missions.size * 4)

    val mainSizeInBytes: Int
        get() = main?.scriptSizeInBytes ?: 0

    val largestMissionSizeInBytes: Int
        get() = missions.sortedWith(compareBy(Script::scriptSizeInBytes)).lastOrNull()?.scriptSizeInBytes ?: 0

    fun addMission(mission: Script) {
        _missions.add(mission)
        addObjectsFromScript(mission)
        addGlobalsFromScript(mission)
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
        return headerSize + mainSizeInBytes + missions.subList(0, index).sumBy { it.scriptSizeInBytes }
    }
}