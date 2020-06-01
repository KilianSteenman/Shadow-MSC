package nl.shadowlink.mission.msc.compiler

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CompilerTest {

    private val compiler = Compiler()

    @Nested
    inner class OpcodeLineTest {

        @Test
        fun `opcode line is parsed as OpcodeLine`() {
            val compiledScript = compiler.compile("01F0: set_max_wanted_level_to 6")

            assertThat(compiledScript.lines.first()).isInstanceOf(OpcodeLine::class.java)
        }

        @Test
        fun `comment is ignored when parsing opcode line`() {
            val line = compiler.compile("0005: \$9 = 304.5 // \$ = float \$COMMENTED_GLOBAL").lines.first()

            val expectedLine = OpcodeLine("0005", listOf(GlobalVar("9"), FloatParam(304.5f)))
            assertThat(line).isEqualTo(expectedLine)
        }

        @Test
        fun `opcode is parsed from opcode line`() {
            val compiledScript = compiler.compile("01F0: set_max_wanted_level_to 6")

            assertThat((compiledScript.lines.first() as OpcodeLine).opcode)
                .isEqualTo("01F0")
        }

        @Test
        fun `when opcode contains multiple int params, the params are added`() {
            val compiledScript = compiler.compile("00C0: set_current_time 12 0")

            assertThat((compiledScript.lines.first() as OpcodeLine).params)
                .containsExactly(
                    IntParam(12),
                    IntParam(0)
                )
        }

        @Nested
        inner class ParseParamTest {

            @Test
            fun `int parameter is parsed`() {
                val compiledScript = compiler.compile("01F0: set_max_wanted_level_to 6")

                assertThat((compiledScript.lines.first() as OpcodeLine).params)
                    .contains(IntParam(6))
            }

            @Test
            fun `float parameter is parsed`() {
                val compiledScript = compiler.compile("04E4: request_collision_at 83.0 -849.8")

                assertThat((compiledScript.lines.first() as OpcodeLine).params)
                    .contains(FloatParam(83.0f))
            }

            @Test
            fun `string parameter is parsed`() {
                val compiledScript = compiler.compile("0352: set_actor \$PLAYER_ACTOR skin_to 'PLAYER'")

                assertThat((compiledScript.lines.first() as OpcodeLine).params)
                    .contains(StringParam("PLAYER"))
            }

            @Test
            fun `global var parameter is parsed`() {
                val compiledScript = compiler.compile("0053: \$PLAYER_CHAR = create_player #NULL at 80.0 -849.8 9.3")

                assertThat((compiledScript.lines.first() as OpcodeLine).params)
                    .contains(GlobalVar("PLAYER_CHAR"))
            }

            @Test
            fun `local var parameter is parsed`() {
                val compiledScript = compiler.compile("0006: 1@ = 2 // @ = int")

                assertThat((compiledScript.lines.first() as OpcodeLine).params)
                    .contains(LocalVar(1))
            }

            @Test
            fun `label parameter is parsed`() {
                val compiledScript = compiler.compile("004D: jump_if_false @MAIN_133")

                assertThat((compiledScript.lines.first() as OpcodeLine).params)
                    .contains(LabelParam("MAIN_133"))
            }

            @Test
            fun `model parameter is parsed`() {
                val compiledScript = compiler.compile("0247: request_model #FAGGIO")

                assertThat((compiledScript.lines.first() as OpcodeLine).params)
                    .contains(ModelParam("FAGGIO"))
            }
        }
    }

    @Nested
    @DisplayName("when line is a label")
    inner class LabelLineTest {

        @Test
        fun `then line is parsed as label line`() {
            val compiledScript = compiler.compile(":MAIN_133")

            assertThat(compiledScript.lines.first()).isInstanceOf(LabelLine::class.java)
        }

        @Test
        fun `then label is set as LabelLine name`() {
            val compiledScript = compiler.compile(":MAIN_133")

            assertThat((compiledScript.lines.first() as LabelLine).name)
                .isEqualTo("MAIN_133")
        }
    }
}