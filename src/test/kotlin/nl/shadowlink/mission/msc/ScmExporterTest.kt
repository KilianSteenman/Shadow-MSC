package nl.shadowlink.mission.msc

import com.google.common.truth.Truth.assertThat
import nl.shadowlink.mission.msc.fakes.FakeBinaryWriter
import org.junit.jupiter.api.Test

internal class ScmExporterTest {

    private val exporter = ScmExporter()

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

    @Test
    fun `script is exported`() {
        val script = CompiledScript().apply {
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