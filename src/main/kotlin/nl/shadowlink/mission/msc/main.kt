package nl.shadowlink.mission.msc

import nl.shadowlink.mission.msc.binarywriter.FileBinaryWriter

fun main(args: Array<String>) {
    val script = Compiler().compile(
        ":Label\n" +
                "03A4: name_thread 'MAIN'\n" +
                "0001: wait\n"
    )
    ScmExporter().export(FileBinaryWriter("/Users/kilian/test.scm"), script)
}