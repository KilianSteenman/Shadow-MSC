package nl.shadowlink.mission.msc.compiler

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CompiledScriptTest {

    @Test
    fun `when a mission is added, then the mission list contains the mission`() {
        val compiledScript = CompiledScript().apply {
            addMission(Script())
        }

        assertThat(compiledScript.missions.size).isEqualTo(1)
    }

    @Test
    fun `total script size is a combination of the header, the main script and all mission scripts combined`() {
        val mainScript = Script().apply {
            addLine(OpcodeLine("0001"))
        }

        val missionScript1 = Script().apply {
            addLine(OpcodeLine("0001"))
        }

        val missionScript2 = Script().apply {
            addLine(OpcodeLine("0001"))
        }

        val compiledScript = CompiledScript().apply {
            main = mainScript
            addMission(missionScript1)
            addMission(missionScript2)
        }

        val expectedSize = compiledScript.headerSize +
                compiledScript.mainSizeInBytes +
                missionScript1.scriptSizeInBytes +
                missionScript2.scriptSizeInBytes

        assertThat(compiledScript.totalSize).isEqualTo(expectedSize)
    }

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
                addMission(smallMission)
                addMission(largeMission)
            }

            assertThat(compiledScript.largestMissionSizeInBytes).isEqualTo(largeMission.scriptSizeInBytes)
        }
    }

    @Nested
    inner class ScriptObjectsTest {

        @Test
        fun `when main script contains an object, then the object is added to the compiled script object list`() {
            val mainScript = Script().apply {
                addObject("object1")
                addObject("object2")
            }

            val compiledScript = CompiledScript().apply {
                main = mainScript
            }

            assertThat(compiledScript.objects).isEqualTo(listOf("object1", "object2"))
        }

        @Test
        fun `when a mission script contains an object, then the object is added to the compiled script object list`() {
            val missionScript = Script().apply {
                addObject("object1")
                addObject("object2")
            }

            val compiledScript = CompiledScript().apply {
                addMission(missionScript)
            }

            assertThat(compiledScript.objects).isEqualTo(listOf("object1", "object2"))
        }

        @Test
        fun `when scripts contain duplicate object entries, then this entry is only added once`() {
            val mainScript = Script().apply {
                addObject("object1")
            }

            val missionScript1 = Script().apply {
                addObject("object1")
                addObject("object2")
            }

            val missionScript2 = Script().apply {
                addObject("object3")
                addObject("object2")
            }

            val compiledScript = CompiledScript().apply {
                main = mainScript
                addMission(missionScript1)
                addMission(missionScript2)
            }

            assertThat(compiledScript.objects).isEqualTo(listOf("object1", "object2", "object3"))
        }
    }

    @Nested
    inner class GlobalsTest {

        @Test
        fun `when main script contains globals, then the globals are added to the compiled script globals list`() {
            val mainScript = Script().apply {
                addGlobal("PLAYER_CHAR")
                addGlobal("PLAYER_ACTOR")
            }

            val compiledScript = CompiledScript().apply {
                main = mainScript
            }

            assertThat(compiledScript.globals).isEqualTo(listOf("PLAYER_CHAR", "PLAYER_ACTOR"))
        }

        @Test
        fun `when mission script contains globals, then the globals are added to the compiled script globals list`() {
            val missionScript = Script().apply {
                addGlobal("PLAYER_CHAR")
                addGlobal("PLAYER_ACTOR")
            }

            val compiledScript = CompiledScript().apply {
                addMission(missionScript)
            }

            assertThat(compiledScript.globals).isEqualTo(listOf("PLAYER_CHAR", "PLAYER_ACTOR"))
        }

        @Test
        fun `when scripts contain duplicate global entries, then this entry is only added once`() {
            val mainScript = Script().apply {
                addGlobal("PLAYER_CHAR")
                addGlobal("PLAYER_ACTOR")
            }

            val missionScript1 = Script().apply {
                addGlobal("PLAYER_CHAR")
                addGlobal("PLAYER_ACTOR")
                addGlobal("VEHICLE")
            }

            val missionScript2 = Script().apply {
                addGlobal("ACTOR_TO_KILL")
                addGlobal("PLAYER_CHAR")
                addGlobal("PLAYER_ACTOR")
                addGlobal("VEHICLE")
            }

            val compiledScript = CompiledScript().apply {
                main = mainScript
                addMission(missionScript1)
                addMission(missionScript2)
            }

            assertThat(compiledScript.globals).isEqualTo(
                listOf(
                    "PLAYER_CHAR",
                    "PLAYER_ACTOR",
                    "VEHICLE",
                    "ACTOR_TO_KILL"
                )
            )
        }
    }

    @Nested
    inner class HeaderSizeTest {

        @Test
        fun `header size is calculated with added objects`() {
            val mainScript = Script().apply {
                // Add some objects, this should increase the header size by 48
                addObject("object1")
                addObject("object2")
            }

            val compiledScript = CompiledScript().apply {
                main = mainScript
            }

            // Header Size = 64 (default header size) + 48 (object size) = 112
            assertThat(compiledScript.headerSize).isEqualTo(112)
        }

        @Test
        fun `header size is calculated with added globals`() {
            val mainScript = Script().apply {
                // Add some globals, this should increase the header size by 8
                addGlobal("PLAYER_CHAR")
                addGlobal("PLAYER_ACTOR")
            }

            val compiledScript = CompiledScript().apply {
                main = mainScript
            }

            // Header Size = 64 (default header size) + 8 (globals) = 72
            assertThat(compiledScript.headerSize).isEqualTo(72)
        }

        @Test
        fun `header size is calculated with added missions`() {
            val mainScript = Script().apply { addLine(OpcodeLine("0001")) }
            val missionScript = Script().apply { addLine(OpcodeLine("0001")) }

            val compiledScript = CompiledScript().apply {
                main = mainScript
                addMission(missionScript)
            }

            // Header Size = 64 (default header size) + 4 (1 mission) = 68
            assertThat(compiledScript.headerSize).isEqualTo(68)
        }
    }

    @Nested
    inner class MissionOffsetTest {

        @Test
        fun `first mission starts after the main script`() {
            val mainScript = Script().apply { addLine(OpcodeLine("0001")) }
            val mission = Script().apply { addLine(OpcodeLine("0001")) }

            val compiledScript = CompiledScript().apply {
                main = mainScript
                addMission(mission)
            }

            val expectedOffset = compiledScript.headerSize + mainScript.scriptSizeInBytes
            assertThat(compiledScript.getOffsetForMission(0)).isEqualTo(expectedOffset)
        }

        @Test
        fun `second missions offset is after the first mission`() {
            val mainScript = Script().apply { addLine(OpcodeLine("0001")) }
            val mission1 = Script().apply { addLine(OpcodeLine("0001")) }
            val mission2 = Script().apply { addLine(OpcodeLine("0001")) }

            val compiledScript = CompiledScript().apply {
                main = mainScript
                addMission(mission1)
                addMission(mission2)
            }

            val expectedOffset = compiledScript.headerSize + mainScript.scriptSizeInBytes + mission1.scriptSizeInBytes
            assertThat(compiledScript.getOffsetForMission(1)).isEqualTo(expectedOffset)
        }
    }

    @Nested
    inner class MainScriptTest {

        @Test
        fun `setting a script as main script will mark it as main`() {
            val mainScript = Script().apply { addLine(OpcodeLine("0001")) }

            val compiledScript = CompiledScript().apply {
                main = mainScript
            }

            assertThat(compiledScript.main?.isMainScript).isTrue()
        }

        @Test
        fun `adding a script as mission, will not mark it as main`() {
            val missionScript = Script().apply { addLine(OpcodeLine("0001")) }

            val compiledScript = CompiledScript().apply {
                addMission(missionScript)
            }

            assertThat(compiledScript.missions.first().isMainScript).isFalse()
        }
    }
}