package nl.shadowlink.mission.msc.compiler

internal class ScriptParser {

    private var tokenizerIndex: Int = 0

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

    private fun parseParams(valueLine: String): List<OpcodeParameter> {
        tokenizerIndex = 0

        val paramList = mutableListOf<OpcodeParameter>()
        val paramMatcherResult = ParamMatcherResult("", 0)
        while (tokenizerIndex < valueLine.length) {
            val matcher = paramMatchers.firstOrNull { matcher ->
                matcher.matches(valueLine, tokenizerIndex, paramMatcherResult)
            }
            
            if (matcher != null) {
                paramList.add(matcher.toParam(paramMatcherResult))
                tokenizerIndex = paramMatcherResult.lastIndex
            } else {
                tokenizerIndex++
            }
        }
        return paramList
    }

    companion object {
        private val labelLineRegex = ":(.+)".toRegex()
        private val opcodeLineRegex = "(.{4}):([^\\/]+)(?:\\/\\/(.+))?".toRegex()

        private val paramMatchers = arrayOf<ParamMatcher>(
            GlobalVarParamMatcher(),
            LocalVarParamMatcher(),
            FloatParamMatcher(),
            IntParamMatcher(),
            GxtParamMatcher(),
            StringParamMatcher(),
            CleoStringParamMatcher(),
            ModelParamMatcher(),
            LabelParamMatcher(),
        )
    }
}