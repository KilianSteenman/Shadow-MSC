package nl.shadowlink.mission.msc.compiler

import com.google.common.truth.Truth.assertThat
import nl.shadowlink.mission.msc.fakes.FakeBinaryWriter
import org.junit.jupiter.api.Test

internal class ScriptWriterTest {

    private val exporter = ScriptWriter()

    @Test
    fun `script is exported`() {
        val script = Script().apply {
            addLine(LabelLine("Main"))
            addLine(OpcodeLine("0001"))
        }

        val bw = FakeBinaryWriter()
        exporter.writeScript(bw, CompiledScript(), script)

        assertThat(bw.writtenBytes).isEqualTo(
            listOf<Byte>(0x01, 0x00)
        )
    }
}