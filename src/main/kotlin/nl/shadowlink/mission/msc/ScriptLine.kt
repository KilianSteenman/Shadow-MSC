package nl.shadowlink.mission.msc

sealed class ScriptLine {
    abstract val sizeInBytes: Int
}

data class OpcodeLine(
    val opcode: String,
    val params: List<OpcodeParameter> = emptyList()
) : ScriptLine() {

    override val sizeInBytes: Int
        get() = 2 + params.sumBy { param -> param.sizeInBytes }
}

data class LabelLine(
    val name: String,
    override val sizeInBytes: Int = 0
) : ScriptLine()