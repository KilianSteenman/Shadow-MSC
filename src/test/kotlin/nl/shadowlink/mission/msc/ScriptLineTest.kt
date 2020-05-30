package nl.shadowlink.mission.msc

import com.google.common.truth.Truth.assertThat
import nl.shadowlink.mission.msc.fakes.FakeBinaryWriter
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ScriptLineTest {

    @Nested
    @DisplayName("when calculating opcode line byte size")
    inner class OpcodeLineTest {

        @Test
        fun `then line without params takes 2 bytes`() {
            val opcodeLine = OpcodeLine("0001")

            assertThat(opcodeLine.sizeInBytes).isEqualTo(2)
        }

        @Test
        fun `then line with params takes 2 bytes + parameter size`() {
            val opcodeLine = OpcodeLine(
                opcode = "0001",
                params = listOf(
                    IntParam(0),
                    FloatParam(1f)
                )
            )

            assertThat(opcodeLine.sizeInBytes).isEqualTo(10)
        }

        @Nested
        inner class WriteTest {

            @Test
            fun `write opcode, writes the opcode`() {
                val opcodeLine = OpcodeLine(opcode = "0001")

                val bw = FakeBinaryWriter()
                opcodeLine.write(bw)

                assertThat(bw.writtenBytes).isEqualTo(
                    listOf<Byte>(0x01, 0x0)
                )
            }
        }
    }

    @Test
    fun `label line byte size is 0`() {
        assertThat(LabelLine("Label").sizeInBytes).isEqualTo(0)
    }
}