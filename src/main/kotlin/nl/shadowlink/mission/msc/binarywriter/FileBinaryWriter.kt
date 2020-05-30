package nl.shadowlink.mission.msc.binarywriter

import java.io.File
import kotlin.experimental.and

class FileBinaryWriter(name: String) : BinaryWriter {

    private val file = File(name)

    override fun writeByte(byte: Byte) {
        file.appendBytes(byteArrayOf(byte))
    }

    @ExperimentalStdlibApi
    override fun writeInt16(value: Short) {
        writeByte((value and 0xff).toByte())
        writeByte(((value.rotateRight(8)) and 0xff).toByte())
    }

    override fun writeInt32(value: Int) {
        TODO("Not yet implemented")
    }

    override fun writeChar(value: Char) {
        writeByte(value.toByte())
    }
}