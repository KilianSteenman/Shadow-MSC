package nl.shadowlink.mission.msc

import nl.shadowlink.mission.msc.binarywriter.FileBinaryWriter

fun main(args: Array<String>) {
    val script = Compiler().compile(":Label\n0001: wait")
    ScmExporter().export(FileBinaryWriter("/Users/kilian/test.scm"), script)
}