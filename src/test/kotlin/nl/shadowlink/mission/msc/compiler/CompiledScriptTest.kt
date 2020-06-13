package nl.shadowlink.mission.msc.compiler

import com.google.common.truth.Truth
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CompiledScriptTest {

    @Nested
    inner class HeaderSize {

        @Test
        fun `header size is calculated with added objects`() {
            val compiledScript = Script().apply {
                // Add some objects, this should increase the header size by 48
                addObject("object1")
                addObject("object2")
            }

            // Header Size = 64 (default header size) + 48 (object size) = 112
            Truth.assertThat(compiledScript.headerSize).isEqualTo(112)
        }

        @Test
        fun `header size is calculated with added globals`() {
            val compiledScript = Script().apply {
                // Add some globals, this should increase the header size by 8
                addGlobal("PLAYER_CHAR")
                addGlobal("PLAYER_ACTOR")
            }

            // Header Size = 64 (default header size) + 8 (globals) = 72
            Truth.assertThat(compiledScript.headerSize).isEqualTo(72)
        }
    }
}