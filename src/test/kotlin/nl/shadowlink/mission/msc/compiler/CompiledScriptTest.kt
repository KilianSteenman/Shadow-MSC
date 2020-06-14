package nl.shadowlink.mission.msc.compiler

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CompiledScriptTest {

    @Nested
    inner class MainScriptSizeTest {

        @Test
        fun `when main script is null, then main script size is 0`() {
            assertThat(CompiledScript().mainSizeInBytes).isEqualTo(0)
        }

        @Test
        fun `main size is the size of the main script`() {
            val mainScript = Script().apply {
                addLine(OpcodeLine("0001", params = listOf(IntParam(0))))
            }

            // Make sure we are not testing with a 0 byte script
            assertThat(mainScript.scriptSizeInBytes).isGreaterThan(0)

            val compiledScript = CompiledScript().apply {
                main = mainScript
            }

            assertThat(compiledScript.mainSizeInBytes).isEqualTo(mainScript.scriptSizeInBytes)
        }
    }

    @Nested
    inner class LargestMissionSizeTest {

        @Test
        fun `when there are no missions, then largest missions size is 0`() {
            assertThat(CompiledScript().largestMissionSizeInBytes).isEqualTo(0)
        }

        @Test
        fun `when there are multiple missions, then largest mission size is returned`() {
            val smallMission = Script().apply {
                addLine(OpcodeLine("0001", params = listOf(IntParam(0))))
            }

            val largeMission = Script().apply {
                addLine(OpcodeLine("0001", params = listOf(IntParam(0))))
                addLine(OpcodeLine("0002", params = listOf(IntParam(0))))
            }

            val compiledScript = CompiledScript().apply {
                missions.add(smallMission)
                missions.add(largeMission)
            }

            assertThat(compiledScript.largestMissionSizeInBytes).isEqualTo(largeMission.scriptSizeInBytes)
        }
    }

    @Nested
    inner class HeaderSizeTest {

        @Test
        fun `header size is calculated with added objects`() {
            val compiledScript = Script().apply {
                // Add some objects, this should increase the header size by 48
                addObject("object1")
                addObject("object2")
            }

            // Header Size = 64 (default header size) + 48 (object size) = 112
            assertThat(compiledScript.headerSize).isEqualTo(112)
        }

        @Test
        fun `header size is calculated with added globals`() {
            val compiledScript = Script().apply {
                // Add some globals, this should increase the header size by 8
                addGlobal("PLAYER_CHAR")
                addGlobal("PLAYER_ACTOR")
            }

            // Header Size = 64 (default header size) + 8 (globals) = 72
            assertThat(compiledScript.headerSize).isEqualTo(72)
        }
    }
}