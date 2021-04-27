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
                val params = parseParams(valueLine)
                script.addLine(OpcodeLine(opcode, params))
            }
        }
        return script
    }

    private class ParamTokenizer {
        var tokenStartIndex = 0
        var value: String? = null

        fun reset() {
            value = null
            tokenStartIndex = 0
        }
    }

    private val paramTokenizer = ParamTokenizer()

    private fun parseParams(valueLine: String): List<OpcodeParameter> {
        paramTokenizer.reset()
        println("Checking: $valueLine")

        val paramList = mutableListOf<OpcodeParameter?>()
        while (paramTokenizer.tokenStartIndex < valueLine.length) {
            when {
                PARAM_GLOBAL.isMatch(valueLine) -> paramList.add(GlobalVar(paramTokenizer.value!!.substring(1)))
                PARAM_LOCAL.isMatch(valueLine) -> paramList.add(
                    LocalVar(paramTokenizer.value!!.substringBefore('@').toInt())
                )
                PARAM_FLOAT.isMatch(valueLine) -> paramList.add(FloatParam(paramTokenizer.value!!.toFloat()))
                PARAM_INT.isMatch(valueLine) -> paramList.add(IntParam(paramTokenizer.value!!.toInt()))
                PARAM_GXT.isMatch(valueLine) -> paramList.add(
                    GxtParam(
                        paramTokenizer.value!!.substringAfter('\"').substringBefore('\"')
                    )
                )
                PARAM_STRING.isMatch(valueLine) -> paramList.add(
                    StringParam(
                        paramTokenizer.value!!.substringAfter('\'').substringBefore('\'')
                    )
                )
                PARAM_CLEO_STRING.isMatch(valueLine) -> paramList.add(
                    CleoStringParam(
                        paramTokenizer.value!!.substringAfter('\"').substringBefore('\"')
                    )
                )
                PARAM_MODEL.isMatch(valueLine) -> paramList.add(ModelParam(paramTokenizer.value!!.substring(1)))
                PARAM_LABEL.isMatch(valueLine) -> paramList.add(LabelParam(paramTokenizer.value!!.substring(1)))
                NO_PARAM.isMatch(valueLine) -> paramList.add(null)
                else -> paramTokenizer.tokenStartIndex++
            }
        }
        return paramList.filterNotNull()
    }

    private fun Regex.isMatch(valueLine: String): Boolean {
        val result = this.find(valueLine, paramTokenizer.tokenStartIndex)
        println("${valueLine.substring(paramTokenizer.tokenStartIndex)} ${this.pattern} StartIndex: ${paramTokenizer.tokenStartIndex} result ${result?.range?.first} ${result?.range?.last}")
        if (result != null && result.range.first == paramTokenizer.tokenStartIndex) {
            paramTokenizer.tokenStartIndex = result.range.last + 1
            paramTokenizer.value = result.value
            return true
        }
        return false
    }

    companion object {
        private val labelLineRegex = ":(.+)".toRegex()
        private val opcodeLineRegex = "(.{4}):([^\\/]+)(?:\\/\\/(.+))?".toRegex()

        private val NO_PARAM = "\\w+".toRegex()
        private val PARAM_INT = "\\d+".toRegex()
        private val PARAM_FLOAT = "-?\\d+\\.\\d+".toRegex()
        private val PARAM_CLEO_STRING = "\".+?\"".toRegex()
        private val PARAM_STRING = "'\\w+'".toRegex()
        private val PARAM_GXT = "\"\\w{1,8}\"".toRegex()
        private val PARAM_GLOBAL = "\\\$\\w+".toRegex()
        private val PARAM_LOCAL = "\\d+@".toRegex()
        private val PARAM_MODEL = "#\\w+".toRegex()
        private val PARAM_LABEL = "@\\w+".toRegex()
    }
}