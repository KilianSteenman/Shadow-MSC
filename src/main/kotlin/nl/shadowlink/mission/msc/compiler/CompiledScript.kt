package nl.shadowlink.mission.msc.compiler

class CompiledScript {

    private val _globals = mutableListOf<String>()
    val globals: List<String> = _globals

    private val _objects = mutableListOf<String>()
    val objects: List<String> = _objects

    val main: Script? = null
    val missions = mutableListOf<Script>()

    val headerSize: Int
        get() = 64 + (objects.size * 24) + (globals.size * 4)

    val mainSizeInBytes: Int = 0
    val largestMissionSizeInBytes: Int = 0
}