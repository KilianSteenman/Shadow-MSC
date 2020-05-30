package nl.shadowlink.mission.msc.binarywriter

interface BinaryWriter {

    fun writeByte(byte: Byte)
    fun writeInt16(value: Short)
    fun writeInt32(value: Int)
    fun writeChar(value: Char)
}