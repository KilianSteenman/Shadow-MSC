package nl.shadowlink.mission.msc

import nl.shadowlink.mission.msc.binarywriter.BinaryWriter

class ScmExporter {

    fun export(bw: BinaryWriter, script: CompiledScript) {
        writeHeader(bw, script)
        writeScript(bw, script)
    }

    fun writeHeader(bw: BinaryWriter, script: CompiledScript) {
        val secondSegmentOffset = 8 + script.globals.size * 4
        bw.writeGoTo()
        bw.writeInt32(secondSegmentOffset)
        bw.writeByte('m'.toByte()) // TODO: Make this the target game
        script.globals.forEach { bw.writeInt32(0) }

        val thirdSegmentOffset = secondSegmentOffset + 36 + script.objects.size * 24
        bw.writeGoTo()
        bw.writeInt32(thirdSegmentOffset)
        bw.writeByte(0) // Alignment
        bw.writeInt32(script.objects.count() + 1) // There is always one dummy object, so increase count by 1
        bw.writeNullTerminatedString("Shadow-MSC", 24) // Dummy object name
        script.objects.forEach { name -> bw.writeNullTerminatedString(name, 24) }

        val fourthSegmentOffset = thirdSegmentOffset + 20 + script.missions.size * 4
        bw.writeGoTo()
        bw.writeInt32(fourthSegmentOffset)
        bw.writeByte(0) // Alignment
        bw.writeInt32(script.scriptSizeInBytes) // Main script size
        bw.writeInt32(0) // TODO: Largest mission size
        bw.writeInt16(script.missions.count().toShort())
        bw.writeInt16(0) // Number of exclusive mission scripts(possibly 1 in III, 2 in VC)
        script.missions.forEach { bw.writeInt32(0) } // TODO: Write mission offsets
    }

    fun writeScript(bw: BinaryWriter, script: CompiledScript) {
        script.lines.forEach { line -> line.write(bw, script) }
    }

    private fun BinaryWriter.writeGoTo() {
        writeByte(0x02)
        writeByte(0x0)
        writeByte(0x01)
    }
}