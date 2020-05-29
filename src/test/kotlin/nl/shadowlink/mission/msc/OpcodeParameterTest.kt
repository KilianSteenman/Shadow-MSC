package nl.shadowlink.mission.msc

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class OpcodeParameterTest {

    @Nested
    inner class IntParamTest {

        @Test
        fun `int parameter takes 4 bytes`() {
            assertThat(IntParam(1).sizeInBytes).isEqualTo(4)
        }
    }

    @Nested
    inner class FloatParamTest {

        @Test
        fun `float parameter takes 4 bytes`() {
            assertThat(FloatParam(1f).sizeInBytes).isEqualTo(4)
        }
    }

    @Nested
    inner class StringParamTest {

        @Test
        fun `when string length is 7, then param takes 7 bytes`() {
            assertThat(StringParam("1234567").sizeInBytes).isEqualTo(7)
        }

        @Test
        fun `when string is shorter than 7 chars, param size is still 7 bytes`() {
            assertThat(StringParam("what").sizeInBytes).isEqualTo(7)
        }
    }

    @Nested
    inner class LabelParamTest {

        @Test
        fun `label parameter takes 4 bytes`() {
            assertThat(LabelParam("LABEL").sizeInBytes).isEqualTo(4)
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