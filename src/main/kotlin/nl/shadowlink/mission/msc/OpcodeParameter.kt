package nl.shadowlink.mission.msc

sealed class OpcodeParameter(
    val sizeInBytes: Int
)

data class IntParam(val value: Int) : OpcodeParameter(sizeInBytes = 4)
data class FloatParam(val value: Float) : OpcodeParameter(sizeInBytes = 0)
data class StringParam(val value: String) : OpcodeParameter(sizeInBytes = 0)
data class LabelParam(val label: String) : OpcodeParameter(sizeInBytes = 0)
data class GlobalVar(val name: String) : OpcodeParameter(sizeInBytes = 0)