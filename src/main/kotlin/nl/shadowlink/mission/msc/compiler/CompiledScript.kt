package nl.shadowlink.mission.msc.compiler

class CompiledScript {

    private val _globals = mutableListOf<String>()
    val globals: List<String> = _globals

    private val _objects = mutableListOf<String>()
    val objects: List<String> = _objects

    var main: Script? = null
    val missions: MutableList<Script> = mutableListOf()

    val headerSize: Int
        get() = 64 + (objects.size * 24) + (globals.size * 4)

    val mainSizeInBytes: Int
        get() = main?.scriptSizeInBytes ?: 0

    val largestMissionSizeInBytes: Int
        get() = missions.sortedWith(compareBy(Script::scriptSizeInBytes)).lastOrNull()?.scriptSizeInBytes ?: 0
}