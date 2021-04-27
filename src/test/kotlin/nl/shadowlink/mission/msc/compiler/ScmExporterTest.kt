package nl.shadowlink.mission.msc.compiler

import com.google.common.truth.Truth.assertThat
import nl.shadowlink.mission.msc.fakes.FakeBinaryWriter
import nl.shadowlink.mission.msc.scm.CompiledScript
import nl.shadowlink.mission.msc.scm.ScmExporter
import nl.shadowlink.mission.msc.scm.export
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ScmExporterTest {

    private val exporter = ScmExporter()

    @Nested
    inner class HeaderTest {

        @Test
        fun `header size matches calculated header size`() {
            val script = Script().apply {
                addLine(LabelLine("Main"))
                addLine(OpcodeLine("0001"))

                addGlobal("PLAYER_CHAR")
                addGlobal("PLAYER_ACTOR")
            }

            val mission = Script().apply {
                addLine(OpcodeLine("0001"))
            }

            val compiledScript = CompiledScript(
                mainScript = script,
                missionScripts = listOf(mission)
            )

            val bw = FakeBinaryWriter()
            exporter.writeHeader(bw, compiledScript)

            assertThat(bw.writtenBytes.size).isEqualTo(compiledScript.headerSize)
        }
    }

    @Test
    fun `script is exported`() {
        val script = Script().apply {
            addLine(LabelLine("Main"))
            addLine(OpcodeLine("0001"))
        }

        val bw = FakeBinaryWriter()
        script.export(bw, CompiledScript(Script()))

        assertThat(bw.writtenBytes).isEqualTo(
            listOf<Byte>(0x01, 0x00)
        )
    }
}