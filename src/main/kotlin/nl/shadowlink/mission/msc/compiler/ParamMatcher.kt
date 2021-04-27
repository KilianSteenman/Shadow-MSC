package nl.shadowlink.mission.msc.compiler

private val PARAM_INT = "(\\d+)".toRegex()
private val PARAM_FLOAT = "(-?\\d+\\.\\d+)".toRegex()
private val PARAM_CLEO_STRING = "\"(.+?)\"".toRegex()
private val PARAM_STRING = "'(\\w+)'".toRegex()
private val PARAM_GXT = "\"(\\w{1,8})\"".toRegex()
private val PARAM_GLOBAL = "\\\$(\\w+)".toRegex()
private val PARAM_LOCAL = "(\\d+)@".toRegex()
private val PARAM_MODEL = "#(\\w+)".toRegex()
private val PARAM_LABEL = "@(\\w+)".toRegex()

internal interface ParamMatcher {

    fun matches(
        valueLine: String,
        tokenizerIndex: Int,
        paramMatcherResult: ParamMatcherResult
    ): Boolean

    fun toParam(paramMatcherResult: ParamMatcherResult): OpcodeParameter
}

internal abstract class RegexParamMatcher(
    private val regex: Regex
) : ParamMatcher {

    override fun matches(
        valueLine: String,
        tokenizerIndex: Int,
        paramMatcherResult: ParamMatcherResult
    ): Boolean {
        val result = regex.find(valueLine, tokenizerIndex)
        if (result != null && result.range.first == tokenizerIndex) {
            paramMatcherResult.lastIndex = result.range.last + 1
            paramMatcherResult.value = result.groupValues[1]
            return true
        }
        return false
    }
}

internal class GlobalVarParamMatcher : RegexParamMatcher(PARAM_GLOBAL) {
    override fun toParam(paramMatcherResult: ParamMatcherResult): OpcodeParameter {
        return GlobalVar(paramMatcherResult.value)
    }
}

internal class LocalVarParamMatcher : RegexParamMatcher(PARAM_LOCAL) {
    override fun toParam(paramMatcherResult: ParamMatcherResult): OpcodeParameter {
        return LocalVar(paramMatcherResult.value.toInt())
    }
}

internal class FloatParamMatcher : RegexParamMatcher(PARAM_FLOAT) {
    override fun toParam(paramMatcherResult: ParamMatcherResult): OpcodeParameter {
        return FloatParam(paramMatcherResult.value.toFloat())
    }
}

internal class IntParamMatcher : RegexParamMatcher(PARAM_INT) {
    override fun toParam(paramMatcherResult: ParamMatcherResult): OpcodeParameter {
        return IntParam(paramMatcherResult.value.toInt())
    }
}

internal class LabelParamMatcher : RegexParamMatcher(PARAM_LABEL) {
    override fun toParam(paramMatcherResult: ParamMatcherResult): OpcodeParameter {
        return LabelParam(paramMatcherResult.value)
    }
}

internal class StringParamMatcher : RegexParamMatcher(PARAM_STRING) {
    override fun toParam(paramMatcherResult: ParamMatcherResult): OpcodeParameter {
        return StringParam(paramMatcherResult.value)
    }
}

internal class CleoStringParamMatcher : RegexParamMatcher(PARAM_CLEO_STRING) {
    override fun toParam(paramMatcherResult: ParamMatcherResult): OpcodeParameter {
        return CleoStringParam(paramMatcherResult.value)
    }
}

internal class ModelParamMatcher : RegexParamMatcher(PARAM_MODEL) {
    override fun toParam(paramMatcherResult: ParamMatcherResult): OpcodeParameter {
        return ModelParam(paramMatcherResult.value)
    }
}

internal class GxtParamMatcher : RegexParamMatcher(PARAM_GXT) {
    override fun toParam(paramMatcherResult: ParamMatcherResult): OpcodeParameter {
        return GxtParam(paramMatcherResult.value)
    }
}

internal data class ParamMatcherResult(
    var value: String,
    var lastIndex: Int
)

