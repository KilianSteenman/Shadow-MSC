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
}