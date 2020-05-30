package nl.shadowlink.mission.msc

import nl.shadowlink.mission.msc.binarywriter.BinaryWriter

sealed class ScriptLine {
    abstract val sizeInBytes: Int

    abstract fun write(bw: BinaryWriter)
}

data class OpcodeLine(
    val opcode: String,
    val params: List<OpcodeParameter> = emptyList()
) : ScriptLine() {

    override val sizeInBytes: Int
        get() = 2 + params.sumBy { param -> param.sizeInBytes }

    override fun write(bw: BinaryWriter) {
        bw.writeInt16(opcode.toShort(16))
    }
}

data class LabelLine(
    val name: String,
    override val sizeInBytes: Int = 0
) : ScriptLine() {

    override fun write(bw: BinaryWriter) {
//        TODO("Not yet implemented")
    }
}