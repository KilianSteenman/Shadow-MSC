package nl.shadowlink.mission.msc.compiler

import nl.shadowlink.mission.msc.binarywriter.BinaryWriter

sealed class ScriptLine {
    abstract val sizeInBytes: Int

    abstract fun write(bw: BinaryWriter, labelOffsetProvider: LabelOffsetProvider, script: Script)
}

data class OpcodeLine(
    val opcode: String,
    val params: List<OpcodeParameter> = emptyList()
) : ScriptLine() {

    override val sizeInBytes: Int
        get() = 2 + params.sumBy { param -> param.sizeInBytes }

    override fun write(bw: BinaryWriter, labelOffsetProvider: LabelOffsetProvider, script: Script) {
        bw.writeUInt16(opcode.toUShort(16))
        params.forEach { it.write(bw, labelOffsetProvider, script) }
    }
}

data class LabelLine(
    val name: String,
    override val sizeInBytes: Int = 0
) : ScriptLine() {

    override fun write(bw: BinaryWriter, labelOffsetProvider: LabelOffsetProvider, script: Script) {
        // Nothing to do here
    }
}