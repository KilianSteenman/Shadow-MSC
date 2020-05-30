package nl.shadowlink.mission.msc.fakes

import nl.shadowlink.mission.msc.binarywriter.BinaryWriter
import java.nio.ByteBuffer
import kotlin.experimental.and

class FakeBinaryWriter : BinaryWriter {

    val writtenBytes = mutableListOf<Byte>()

    override fun writeByte(byte: Byte) {
        writtenBytes.add(byte)
    }

    @ExperimentalStdlibApi
    override fun writeInt16(value: Short) {
        writeByte((value and 0xff).toByte())
        writeByte(((value.rotateRight(8)) and 0xff).toByte())
    }

    override fun writeInt32(value: Int) {
        with(ByteBuffer.allocate(4).putInt(value).array()) {
            writeByte(get(3))
            writeByte(get(2))
            writeByte(get(1))
            writeByte(get(0))
        }
    }

    override fun writeChar(value: Char) {
        writeByte(value.toByte())
    }

    override fun writeNullTerminatedString(value: String, length: Int) {
        value.forEach { writeChar(it) }
        writeByte(0) // Zero termination
        repeat((value.length + 1 until length).count()) { writeByte(0x0) } // Add padding to fill length
    }
}
