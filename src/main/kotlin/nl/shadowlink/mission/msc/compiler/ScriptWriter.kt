package nl.shadowlink.mission.msc.compiler

import nl.shadowlink.mission.msc.binarywriter.BinaryWriter

internal class ScriptWriter {

    fun writeScript(bw: BinaryWriter, compiledScript: CompiledScript, script: Script) {
        script.lines.forEach { line -> line.write(bw, compiledScript, script) }
    }
}