package nl.shadowlink.mission.msc.compiler

import com.google.common.truth.Truth.assertThat
import nl.shadowlink.mission.msc.fakes.FakeBinaryWriter
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class OpcodeParameterTest {

    @Nested
    inner class IntParamTest {

        @Test
        fun `int parameter takes 5 bytes including type byte`() {
            assertThat(IntParam(1).sizeInBytes).isEqualTo(5)
        }

        @Test
        fun `int is written`() {
            val bw = FakeBinaryWriter()

            IntParam(1).write(bw, CompiledScript())

            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x1) + // Type
                listOf<Byte>(0x1, 0x0, 0x0, 0x0) // Value
            )
        }
    }

    @Nested
    inner class FloatParamTest {

        @Test
        fun `float parameter takes 5 bytes including type byte`() {
            assertThat(FloatParam(1f).sizeInBytes).isEqualTo(5)
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
        fun `when string length is 7, then param takes 8 bytes`() {
            assertThat(StringParam("1234567").sizeInBytes).isEqualTo(8)
        }

        @Test
        fun `when string is shorter than 7 chars, then param takes 8 bytes`() {
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
        fun `when string is 7 bytes, then string is written without padding`() {
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
        fun `label parameter takes 5 bytes including type byte`() {
            assertThat(LabelParam("LABEL").sizeInBytes).isEqualTo(5)
        }

        @Test
        fun `address parameter is written at address offset + header size`() {
            val bw = FakeBinaryWriter()

            val script = CompiledScript().apply {
                addLine(OpcodeLine("0001"))
                addLine(LabelLine("Label"))
            }
            LabelParam("Label").write(bw, script)

            // Header size:     64
            // Offset:          2
            // LabelAddress:    66 (0x42)
            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x1) + // Type
                listOf<Byte>(0x42, 0x0, 0x0, 0x0)
            )
        }
    }

    @Nested
    inner class GlobalVarParamTest {

        @Test
        fun `global var parameter takes 3 bytes including type byte`() {
            assertThat(GlobalVar("TEST_PARAM").sizeInBytes).isEqualTo(3)
        }

        @Test
        fun `global var parameter is written`() {
            val script = CompiledScript().apply {
                addGlobal("PLAYER_CHAR")
                addGlobal("VEHICLE")
            }

            // First param should be at address 0
            with(FakeBinaryWriter()) {
                GlobalVar("PLAYER_CHAR").write(this, script)

                assertThat(this.writtenBytes).isEqualTo(
                    listOf<Byte>(0x02, 0x0, 0x0)
                )
            }

            // Second param should be at address 4
            with(FakeBinaryWriter()) {
                GlobalVar("VEHICLE").write(this, script)

                assertThat(this.writtenBytes).isEqualTo(
                    listOf<Byte>(0x02, 0x4, 0x0)
                )
            }
        }

        @Test
        fun `global var param type is written`() {
            val bw = FakeBinaryWriter()
            val script = CompiledScript().apply {
                addGlobal("PLAYER_CHAR")
            }

            GlobalVar("PLAYER_CHAR").write(bw, script)

            assertThat(bw.writtenBytes.first()).isEqualTo(0x02.toByte())
        }
    }
}