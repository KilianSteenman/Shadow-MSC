package nl.shadowlink.mission.msc.compiler

import nl.shadowlink.mission.msc.binarywriter.BinaryWriter

class ScmExporter {

    fun export(bw: BinaryWriter, compiledScript: CompiledScript) {
        writeHeader(bw, compiledScript)

        compiledScript.mainScript.export(bw, compiledScript)
        compiledScript.missionScripts.forEach { mission -> mission.export(bw, compiledScript) }
    }

    fun writeHeader(bw: BinaryWriter, script: CompiledScript) {
        println("Writing header (${script.headerSize} bytes)")
        val secondSegmentOffset = 8 + script.globals.size * 4
        with(bw) {
            writeGoTo()
            writeInt32(secondSegmentOffset)
            writeByte('m'.toByte()) // TODO: Make this the target game
            repeat(script.globals.size) { writeInt32(0) }
        }

        val thirdSegmentOffset = secondSegmentOffset + 36 + script.objects.size * 24
        with(bw) {
            writeGoTo()
            writeInt32(thirdSegmentOffset)
            writeByte(0) // Alignment
            writeInt32(script.objects.count() + 1) // There is always one dummy object, so increase count by 1
            writeNullTerminatedString("Shadow-MSC", 24) // Dummy object name
            script.objects.forEach { name -> writeNullTerminatedString(name, 24) }
        }

        val fourthSegmentOffset = thirdSegmentOffset + 20 + script.missionScripts.size * 4
        with(bw) {
            writeGoTo()
            writeInt32(fourthSegmentOffset)
            writeByte(0) // Alignment
            writeInt32(script.mainSizeInBytes) // Main script size
            writeInt32(script.largestMissionSizeInBytes)
            writeInt16(script.missionScripts.count().toShort())
            writeInt16(0) // Number of exclusive mission scripts(possibly 1 in III, 2 in VC)
            script.missionScripts.forEachIndexed { index, _ -> writeInt32(script.getOffsetForMission(index)) }
        }
    }

    private fun BinaryWriter.writeGoTo() {
        writeByte(0x02)
        writeByte(0x0)
        writeByte(0x01)
    }
}

fun Script.export(bw: BinaryWriter, compiledScript: CompiledScript) {
    lines.forEach { line -> line.write(bw, compiledScript, this) }
}