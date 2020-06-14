package nl.shadowlink.mission.msc.compiler

internal class ScriptParser {

    fun parse(source: String): Script {
        val script = Script()

        source.lines().forEach { line ->
            labelLineRegex.matchEntire(line)?.let { labelResult ->
                val (labelName) = labelResult.destructured
                script.addLine(LabelLine(labelName))
            }

            opcodeLineRegex.matchEntire(line)?.let { result ->
                val (opcode, valueLine) = result.destructured
                val params = paramSplitRegex.findAll(valueLine)
                    .map { paramResult -> parseParam(paramResult) }
                    .filterNotNull()
                    .toList()

                script.addLine(OpcodeLine(opcode, params))
            }
        }
        return script
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
            resultString.startsWith('\'') -> StringParam(resultString.substring(1, resultString.lastIndex))
            else -> null
        }
    }

    companion object {
        private val labelLineRegex = ":(.+)".toRegex()
        private val opcodeLineRegex = "(.{4}):([^\\/]+)(?:\\/\\/(.+))?".toRegex()
        private val paramSplitRegex = "(\\S+)".toRegex()
    }
}