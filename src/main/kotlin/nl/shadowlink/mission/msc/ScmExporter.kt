package nl.shadowlink.mission.msc

import nl.shadowlink.mission.msc.binarywriter.BinaryWriter

class ScmExporter {

    fun export(bw: BinaryWriter, script: CompiledScript) {
        writeHeader(bw, script)
        writeScript(bw, script)
    }

    fun writeHeader(bw: BinaryWriter, script: CompiledScript) {
        with(bw) {
            writeByte(0x02)
            writeByte(0x0)
            writeByte(0x01)
        }
    }

    fun writeScript(bw: BinaryWriter, script: CompiledScript) {
        script.lines.forEach { line -> line.write(bw) }
    }
}