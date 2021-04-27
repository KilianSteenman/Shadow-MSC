package nl.shadowlink.mission.msc.compiler

import com.google.common.truth.Truth.assertThat
import nl.shadowlink.mission.msc.fakes.FakeBinaryWriter
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class OpcodeParameterTest {

    private val emptyMainScript = Script()
    
    @Nested
    inner class IntParamTest {

        @Test
        fun `when int fits into a single byte, then size is 2`() {
            assertThat(IntParam(1).sizeInBytes).isEqualTo(2)
        }

        @Test
        fun `when int fits into an Int16, then size is 3`() {
            assertThat(IntParam(32767).sizeInBytes).isEqualTo(3)
        }

        @Test
        fun `when int fits into an Int32, then size is 5`() {
            assertThat(IntParam(32768).sizeInBytes).isEqualTo(5)
        }

        @Test
        fun `int is written`() {
            val bw = FakeBinaryWriter()

            IntParam(32768).write(bw, CompiledScript(emptyMainScript), Script())

            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x1) + // Type
                        listOf<Byte>(0x0, 0x80.toByte(), 0x0, 0x0) // Value
            )
        }

        @Test
        fun `when int param fits into a single byte, param is written as byte`() {
            val bw = FakeBinaryWriter()

            IntParam(0).write(bw, CompiledScript(emptyMainScript), Script())

            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x4) + // Type
                        listOf<Byte>(0x0) // Value
            )
        }

        @Test
        fun `-128 is the lowest number that is written as single byte`() {
            val bw = FakeBinaryWriter()

            IntParam(-128).write(bw, CompiledScript(emptyMainScript), Script())

            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x4) + // Type
                        listOf<Byte>(0x80.toByte()) // Value
            )
        }

        @Test
        fun `127 is the highest number that is written as single byte`() {
            val bw = FakeBinaryWriter()

            IntParam(127).write(bw, CompiledScript(emptyMainScript), Script())

            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x4) + // Type
                        listOf<Byte>(0x7F.toByte()) // Value
            )
        }

        @Test
        fun `-32768 is the lowest number that is written as Int16`() {
            val bw = FakeBinaryWriter()

            IntParam(-32768).write(bw, CompiledScript(emptyMainScript), Script())

            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x5) + // Type
                        listOf<Byte>(0x0, 0x80.toByte()) // Value
            )
        }

        @Test
        fun `32767 is the highest number that is written as Int16`() {
            val bw = FakeBinaryWriter()

            IntParam(32767).write(bw, CompiledScript(emptyMainScript), Script())

            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x5) + // Type
                        listOf<Byte>(0xFF.toByte(), 0x7F.toByte()) // Value
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

            FloatParam(1f).write(bw, CompiledScript(emptyMainScript), Script())

            assertThat(bw.writtenBytes.first()).isEqualTo(
                0x06.toByte()
            )
        }

        @Test
        fun `float param type is written`() {
            val bw = FakeBinaryWriter()

            FloatParam(3.2f).write(bw, CompiledScript(emptyMainScript), Script())

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

            StringParam("MAIN").write(bw, CompiledScript(emptyMainScript), Script())

            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x4D, 0x41, 0x49, 0x4E) + // MAIN
                        listOf<Byte>(0x0) + // Zero termination
                        listOf<Byte>(0xCC.toByte(), 0xCC.toByte(), 0xCC.toByte()) // Padding
            )
        }

        @Test
        fun `when string is 7 bytes, then string is written without padding`() {
            val bw = FakeBinaryWriter()

            StringParam("INITIAL").write(bw, CompiledScript(emptyMainScript), Script())

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
        fun `when script is the main script, then label parameter is written as absolute file offset`() {
            val bw = FakeBinaryWriter()

            val script = Script().apply {
                isMainScript = true
                addLine(OpcodeLine("0001"))
                addLine(LabelLine("Label"))
            }
            LabelParam("Label").write(bw, CompiledScript(emptyMainScript), script)

            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x1) + // Type
                        listOf<Byte>(0x42, 0x0, 0x0, 0x0)
            )
        }

        @Test
        fun `when script is not the main script, then label parameter is written as relative script offset`() {
            val bw = FakeBinaryWriter()

            val script = Script().apply {
                addLine(OpcodeLine("0001"))
                addLine(LabelLine("Label"))
            }
            LabelParam("Label").write(bw, CompiledScript(emptyMainScript), script)

            assertThat(bw.writtenBytes).isEqualTo(
                listOf<Byte>(0x1) + // Type
                        listOf<Byte>(0xFE.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
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
            val script = Script().apply {
                addGlobal("PLAYER_CHAR")
                addGlobal("VEHICLE")
            }

            // First param should be at address 0
            with(FakeBinaryWriter()) {
                GlobalVar("PLAYER_CHAR").write(this, CompiledScript(emptyMainScript), script)

                assertThat(this.writtenBytes).isEqualTo(
                    listOf<Byte>(0x02, 0x0, 0x0)
                )
            }

            // Second param should be at address 4
            with(FakeBinaryWriter()) {
                GlobalVar("VEHICLE").write(this, CompiledScript(emptyMainScript), script)

                assertThat(this.writtenBytes).isEqualTo(
                    listOf<Byte>(0x02, 0x4, 0x0)
                )
            }
        }

        @Test
        fun `global var param type is written`() {
            val bw = FakeBinaryWriter()
            val script = Script().apply {
                addGlobal("PLAYER_CHAR")
            }

            GlobalVar("PLAYER_CHAR").write(bw, CompiledScript(emptyMainScript), script)

            assertThat(bw.writtenBytes.first()).isEqualTo(0x02.toByte())
        }
    }

    @Nested
    inner class LocalVarTest {

        @Test
        fun `local var parameter takes 3 bytes including type byte`() {
            assertThat(LocalVar(0).sizeInBytes).isEqualTo(3)
        }

        @Test
        fun `local var parameter is written`() {
            // Param with index 0
            with(FakeBinaryWriter()) {
                LocalVar(0).write(this, CompiledScript(emptyMainScript), Script())

                assertThat(this.writtenBytes).isEqualTo(
                    listOf<Byte>(0x03, 0x0, 0x0)
                )
            }

            // Param with index 1
            with(FakeBinaryWriter()) {
                LocalVar(1).write(this, CompiledScript(emptyMainScript), Script())

                assertThat(this.writtenBytes).isEqualTo(
                    listOf<Byte>(0x03, 0x2, 0x0)
                )
            }
        }

        @Test
        fun `local var param type is written`() {
            val bw = FakeBinaryWriter()

            LocalVar(1).write(bw, CompiledScript(emptyMainScript), Script())

            assertThat(bw.writtenBytes.first()).isEqualTo(0x03.toByte())
        }
    }

    @Nested
    inner class ModelParamTest {

        private val script = Script()

        @Test
        fun `model parameter takes 5 bytes including type byte`() {
            assertThat(ModelParam("FAGGIO").sizeInBytes).isEqualTo(5)
        }

        @Test
        fun `model parameter is written`() {
            with(FakeBinaryWriter()) {
                ModelParam("FAGGIO").write(this, CompiledScript(emptyMainScript), script)

                assertThat(this.writtenBytes).isEqualTo(
                    listOf<Byte>(0x01, 0xC0.toByte(), 0x0, 0x0, 0x0)
                )
            }
        }

        @Test
        fun `model param type is written`() {
            val bw = FakeBinaryWriter()

            ModelParam("FAGGIO").write(bw, CompiledScript(emptyMainScript), script)

            assertThat(bw.writtenBytes.first()).isEqualTo(0x01.toByte())
        }
    }

    @Nested
    inner class CleoStringParamTest {

        @Test
        fun `cleo string parameter takes string length bytes plus type byte`() {
            assertThat(CleoStringParam("Hello World!").sizeInBytes).isEqualTo(13)
        }

        @Test
        fun `param type is written`() {
            val bw = FakeBinaryWriter()

            CleoStringParam("Hello World!").write(bw, CompiledScript(emptyMainScript), Script())

            assertThat(bw.writtenBytes.first()).isEqualTo(0x0e.toByte())
        }

        @Test
        fun `param is written`() {
            val bw = FakeBinaryWriter()

            CleoStringParam("Hello World!").write(bw, CompiledScript(emptyMainScript), Script())

            val expectedOutput = byteArrayOf(
                0x0e, 0x0c, 0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x20, 0x57, 0x6f, 0x72, 0x6c, 0x64, 0x21
            )
            assertThat(bw.writtenBytes.toByteArray()).isEqualTo(expectedOutput)
        }
    }
}