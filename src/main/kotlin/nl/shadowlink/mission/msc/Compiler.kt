package nl.shadowlink.mission.msc

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
            resultString.startsWith('\'') -> StringParam(resultString.substring(1, resultString.lastIndex))
            resultString.startsWith("@") -> LabelParam(resultString.substring(1))
            else -> null
        }
    }

    companion object {
        private val labelLineRegex = ":(.+)".toRegex()
        private val opcodeLineRegex = "(.{4}):(.+)".toRegex()
        private val paramSplitRegex = "(\\S+)".toRegex()
    }
}

open class CompiledScript {

    val headerSize: Int
        get() = 64 + objects.size * 24

    val missions: List<CompiledScript> = emptyList()
    val lines = mutableListOf<ScriptLine>()

    val globals: List<Int> = emptyList()

    private val _objects = mutableListOf<String>()
    val objects: List<String> = _objects

    val scriptSizeInBytes: Int
        get() = lines.sumBy { line -> line.sizeInBytes }

    fun addLine(line: ScriptLine) {
        lines.add(line)
    }

    fun getAddressForLabel(label: String): Int {
        return 0
    }

    fun addObject(name: String) {
        if (!_objects.contains(name)) {
            _objects.add(name)
        }
    }
}