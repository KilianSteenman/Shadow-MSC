package nl.shadowlink.mission.msc

sealed class ScriptLine

data class OpcodeLine(
    val opcode: String,
    val params: List<OpcodeParameter>
) : ScriptLine()

data class LabelLine(
    val name: String
) : ScriptLine()