package nl.shadowlink.mission.msc.compiler

import com.google.common.truth.Truth.assertThat
import nl.shadowlink.mission.msc.fakes.FakeBinaryWriter
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

            val compiledScript = CompiledScript().apply {
                main = script
            }

            val bw = FakeBinaryWriter()
            exporter.writeHeader(bw, compiledScript)

            assertThat(bw.writtenBytes.size).isEqualTo(script.headerSize)
        }

        @Test
        fun `header is exported`() {
//        val script = CompiledScript().apply {
//            addLine(LabelLine("Main"))
//            addLine(OpcodeLine("0001"))
//        }
//
//        val bw = FakeBinaryWriter()
//        exporter.writeHeader(bw, script)
//
//        assertThat(bw.writtenBytes).containsExactly(
//            listOf<Byte>(0x02, 0x0, 0x01)
//        )
        }
    }

    @Test
    fun `script is exported`() {
        val script = Script().apply {
            addLine(LabelLine("Main"))
            addLine(OpcodeLine("0001"))
        }

        val bw = FakeBinaryWriter()
        exporter.writeScript(bw, script)

        assertThat(bw.writtenBytes).isEqualTo(
            listOf<Byte>(0x01, 0x00)
        )
    }
}