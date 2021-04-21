package nl.shadowlink.mission.msc.compiler

import nl.shadowlink.mission.msc.binarywriter.BinaryWriter
import java.nio.ByteBuffer

sealed class OpcodeParameter(
    open val sizeInBytes: Int
) {
    abstract fun write(bw: BinaryWriter, labelOffsetProvider: LabelOffsetProvider, script: Script)
}

data class IntParam(val value: Int) : OpcodeParameter(sizeInBytes = 0) {

    override val sizeInBytes: Int
        get() = when {
            value.fitsIntoSignedByte() -> 2
            value.fitsIntoInt16() -> 3
            else -> 5
        }

    override fun write(bw: BinaryWriter, labelOffsetProvider: LabelOffsetProvider, script: Script) {
        when {
            value.fitsIntoSignedByte() -> bw.writeByteParam(value.toByte())
            value.fitsIntoInt16() -> bw.writeInt16Param(value.toShort())
            else -> bw.writeInt32Param(value)
        }
    }

    private fun Int.fitsIntoSignedByte(): Boolean {
        return this >= -128 && this <= 127
    }

    private fun Int.fitsIntoInt16(): Boolean {
        return this >= -32768 && this <= 32767
    }

    private fun BinaryWriter.writeByteParam(value: Byte) {
        writeByte(0x4) // Type
        writeByte(value)
    }

    private fun BinaryWriter.writeInt16Param(value: Short) {
        writeByte(0x5) // Type
        writeInt16(value)
    }

    private fun BinaryWriter.writeInt32Param(value: Int) {
        writeByte(0x1) // Type
        writeInt32(value)
    }
}

data class FloatParam(val value: Float) : OpcodeParameter(sizeInBytes = 5) {
    override fun write(bw: BinaryWriter, labelOffsetProvider: LabelOffsetProvider, script: Script) {
        bw.writeByte(0x06) // Type
        with(ByteBuffer.allocate(4).putFloat(value).array()) {
            bw.writeByte(get(3))
            bw.writeByte(get(2))
            bw.writeByte(get(1))
            bw.writeByte(get(0))
        }
    }
}

data class StringParam(val value: String) : OpcodeParameter(sizeInBytes = 8) {
    override fun write(bw: BinaryWriter, labelOffsetProvider: LabelOffsetProvider, script: Script) {
        value.forEach { bw.writeChar(it) }
        bw.writeByte(0) // Zero termination
        repeat((value.length + 1 until 8).count()) { bw.writeByte(0xCC.toByte()) }
    }
}

data class LabelParam(val label: String) : OpcodeParameter(sizeInBytes = 5) {
    override fun write(bw: BinaryWriter, labelOffsetProvider: LabelOffsetProvider, script: Script) {
        bw.writeByte(0x1) // Type
        bw.writeInt32(script.getAddressForLabel(label, labelOffsetProvider.labelOffset))
    }
}

data class GlobalVar(val name: String) : OpcodeParameter(sizeInBytes = 3) {
    override fun write(bw: BinaryWriter, labelOffsetProvider: LabelOffsetProvider, script: Script) {
        bw.writeByte(0x2) // Type
        bw.writeInt16(script.getAddressForGlobal(name))
    }
}

data class LocalVar(val index: Int) : OpcodeParameter(sizeInBytes = 3) {
    override fun write(bw: BinaryWriter, labelOffsetProvider: LabelOffsetProvider, script: Script) {
        bw.writeByte(0x3)
        bw.writeInt16((index * 2).toShort())
    }
}

data class ModelParam(val name: String) : OpcodeParameter(sizeInBytes = 5) {
    override fun write(bw: BinaryWriter, labelOffsetProvider: LabelOffsetProvider, script: Script) {
        bw.writeByte(0x1)
        bw.writeInt32(script.getIdForModel(name))
    }
}