package nl.shadowlink.mission.msc

import nl.shadowlink.mission.msc.binarywriter.BinaryWriter

sealed class OpcodeParameter(
    val sizeInBytes: Int
) {
    abstract fun write(bw: BinaryWriter)
}

data class IntParam(val value: Int) : OpcodeParameter(sizeInBytes = 4) {
    override fun write(bw: BinaryWriter) {
        bw.writeInt32(value)
    }
}

data class FloatParam(val value: Float) : OpcodeParameter(sizeInBytes = 4) {
    override fun write(bw: BinaryWriter) {
        TODO("Not yet implemented")
    }
}

data class StringParam(val value: String) : OpcodeParameter(sizeInBytes = 8) {
    override fun write(bw: BinaryWriter) {
        value.forEach { bw.writeChar(it) }
        bw.writeByte(0) // Zero termination
        repeat((value.length + 1 until 8).count()) { bw.writeByte(0xCC.toByte()) }
    }
}

data class LabelParam(val label: String) : OpcodeParameter(sizeInBytes = 4) {
    override fun write(bw: BinaryWriter) {
        TODO("Not yet implemented")
    }
}

data class GlobalVar(val name: String) : OpcodeParameter(sizeInBytes = 2) {
    override fun write(bw: BinaryWriter) {
        TODO("Not yet implemented")
    }
}
