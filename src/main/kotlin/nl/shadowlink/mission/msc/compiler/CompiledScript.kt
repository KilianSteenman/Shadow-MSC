package nl.shadowlink.mission.msc.compiler

import nl.shadowlink.mission.msc.gamedata.VcGameData

class CompiledScript {

    private val gameData = VcGameData()

    private var currentAddress = 0
    private val labelAddressMapping = mutableMapOf<String, Int>()

    val headerSize: Int
        get() = 64 + (objects.size * 24) + (globals.size * 4)

    val missions: List<CompiledScript> = emptyList()
    val lines = mutableListOf<ScriptLine>()

    private val _globals = mutableListOf<String>()
    val globals: List<String> = _globals

    private val _objects = mutableListOf<String>()
    val objects: List<String> = _objects

    val scriptSizeInBytes: Int
        get() = lines.sumBy { line -> line.sizeInBytes }

    fun addLine(line: ScriptLine) {
        lines.add(line)

        if (line is OpcodeLine) {
            line.params.forEach { param ->
                if (param is GlobalVar) {
                    addGlobal(param.name)
                }
            }
        }

        calculateLabelAddress(line)
    }

    fun getAddressForLabel(label: String): Int {
        return labelAddressMapping[label] ?: throw IllegalStateException("Unable to find address for label [$label]")
    }

    fun addObject(name: String) {
        if (!_objects.contains(name)) {
            _objects.add(name)
        }
    }

    private fun calculateLabelAddress(line: ScriptLine) {
        when (line) {
            is LabelLine -> labelAddressMapping[line.name] = currentAddress
            else -> currentAddress += line.sizeInBytes
        }
    }

    fun addGlobal(name: String) {
        if (!_globals.contains(name)) {
            _globals.add(name)
        }
    }

    fun getAddressForGlobal(name: String): Short {
        return (_globals.indexOf(name) * 4).toShort()
    }

    fun getIdForModel(name: String): Int {
        return gameData.getIdForModel(name)
    }
}