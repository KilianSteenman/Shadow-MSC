package nl.shadowlink.mission.msc.fakes

import nl.shadowlink.mission.msc.binarywriter.BinaryWriter
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
//        writeByte((value and 0xff).toByte())
//        writeByte(((value.rotateRight(8)) and 0xff).toByte())
    }

    override fun writeChar(value: Char) {
        writeByte(value.toByte())
    }
}
