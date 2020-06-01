package nl.shadowlink.mission.msc.compiler

class Compiler {

    fun compile(input: String): CompiledScript {
        val compiledScript = CompiledScript()

        input.lines().forEach { line ->
            labelLineRegex.matchEntire(line)?.let { labelResult ->
                val (labelName) = labelResult.destructured
                compiledScript.addLine(LabelLine(labelName))
            }

            opcodeLineRegex.matchEntire(line)?.let { result ->
                val (opcode, valueLine) = result.destructured
                val params = paramSplitRegex.findAll(valueLine)
                    .map { paramResult -> parseParam(paramResult) }
                    .filterNotNull()
                    .toList()

                compiledScript.addLine(OpcodeLine(opcode, params))
            }
        }
        return compiledScript
    }

    private fun parseParam(paramResult: MatchResult): OpcodeParameter? {
        val resultString = paramResult.value
        val intParam = resultString.toIntOrNull()
        val floatParam = resultString.toFloatOrNull()
        return when {
            intParam != null -> IntParam(intParam)
            floatParam != null -> FloatParam(floatParam)
            resultString.startsWith("$") -> GlobalVar(resultString.substring(1))
            resultString.endsWith('@') -> LocalVar(resultString.substringBefore('@').toInt())
            resultString.startsWith("@") -> LabelParam(resultString.substringAfter('@'))
            resultString.startsWith("#") -> ModelParam(resultString.substringAfter('#'))
            resultString.startsWith('\'') -> StringParam(
                resultString.substring(1, resultString.lastIndex)
            )
            else -> null
        }
    }

    companion object {
        private val labelLineRegex = ":(.+)".toRegex()
        private val opcodeLineRegex = "(.{4}):([^\\/]+)(?:\\/\\/(.+))?".toRegex()
        private val paramSplitRegex = "(\\S+)".toRegex()
    }
}

open class CompiledScript {

    private var currentAddress = 0
    private val labelAddressMapping = mutableMapOf<String, Int>()

    val headerSize: Int
        get() = 64 + objects.size * 24

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
}