package nl.shadowlink.mission.msc

import com.google.common.truth.Truth.assertThat
import nl.shadowlink.mission.msc.fakes.FakeBinaryWriter
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class OpcodeParameterTest {

    @Nested
    inner class IntParamTest {

        @Test
        fun `int parameter takes 4 bytes`() {
            assertThat(IntParam(1).sizeInBytes).isEqualTo(4)
        }

        @Test
        fun `int is written`() {
            val bw = FakeBinaryWriter()

            IntParam(1).write(bw, CompiledScript())

            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x1, 0x0, 0x0, 0x0)
            )
        }
    }

    @Nested
    inner class FloatParamTest {

        @Test
        fun `float parameter takes 4 bytes`() {
            assertThat(FloatParam(1f).sizeInBytes).isEqualTo(4)
        }

        @Test
        fun `float param type is 6`() {
            val bw = FakeBinaryWriter()

            FloatParam(1f).write(bw, CompiledScript())

            assertThat(bw.writtenBytes.first()).isEqualTo(
                0x06.toByte()
            )
        }

        @Test
        fun `float param type is written`() {
            val bw = FakeBinaryWriter()

            FloatParam(3.2f).write(bw, CompiledScript())

            assertThat(bw.writtenBytes).isEqualTo(
                listOf(0x06, 0xCD.toByte(), 0xCC.toByte(), 0x4C, 0x40)
            )
        }
    }

    @Nested
    inner class StringParamTest {

        @Test
        fun `when string length is 7, then param takes 7 bytes`() {
            assertThat(StringParam("1234567").sizeInBytes).isEqualTo(8)
        }

        @Test
        fun `when string is shorter than 7 chars, param size is still 7 bytes`() {
            assertThat(StringParam("what").sizeInBytes).isEqualTo(8)
        }

        @Test
        fun `string is written and padded`() {
            val bw = FakeBinaryWriter()

            StringParam("MAIN").write(bw, CompiledScript())

            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x4D, 0x41, 0x49, 0x4E) + // MAIN
                        listOf<Byte>(0x0) + // Zero termination
                        listOf<Byte>(0xCC.toByte(), 0xCC.toByte(), 0xCC.toByte()) // Padding
            )
        }

        @Test
        fun `string is written without padding`() {
            val bw = FakeBinaryWriter()

            StringParam("INITIAL").write(bw, CompiledScript())

            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x49, 0x4E, 0x49, 0x54, 0x49, 0x41, 0x4C) + // INITIAL
                        listOf<Byte>(0x0) // Zero termination
            )
        }
    }

    @Nested
    inner class LabelParamTest {

        @Test
        fun `label parameter takes 4 bytes`() {
            assertThat(LabelParam("LABEL").sizeInBytes).isEqualTo(4)
        }

        @Test
        fun `address parameter is written`() {
            val bw = FakeBinaryWriter()

            val script = CompiledScript().apply {
                addLine(OpcodeLine("0001"))
                addLine(LabelLine ("Label"))
            }
            LabelParam("Label").write(bw, script)

            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x2, 0x0, 0x0, 0x0)
            )
        }
    }

    @Nested
    inner class GlobalVarParamTest {

        @Test
        fun `global var parameter takes 2 bytes`() {
            assertThat(GlobalVar("TEST_PARAM").sizeInBytes).isEqualTo(2)
        }
    }
}