package nl.shadowlink.mission.msc

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CompiledScriptTest {

    @Nested
    inner class LabelAddressMappingTest {

        @Test
        fun `label addresses is mapped`() {
            val compiledScript = CompiledScript()
            compiledScript.apply {
                addLine(LabelLine("INIT"))
            }

            assertThat(compiledScript.getAddressForLabel("INIT")).isEqualTo(0)
        }

        @Test
        fun `multiple label addresses are mapped`() {
            val compiledScript = CompiledScript()
            compiledScript.apply {
                addLine(LabelLine("INIT"))
                addLine(OpcodeLine("0001"))
                addLine(LabelLine("MAIN"))
            }

            assertThat(compiledScript.getAddressForLabel("INIT")).isEqualTo(0)
            assertThat(compiledScript.getAddressForLabel("MAIN")).isEqualTo(2)
        }
    }

    @Test
    fun `total script size is calculated`() {
        val compiledScript = CompiledScript()
        compiledScript.apply {
            addLine(LabelLine("INIT"))                                   // Size: 0 (total 0)
            addLine(OpcodeLine("0001"))                                 // Size: 2 (total 2)
            addLine(OpcodeLine("0002", listOf(IntParam(0))))      // Size: 7 (total 9)
            addLine(LabelLine("INIT_2"))                                  // Size: 0 (total 9)
            addLine(OpcodeLine("0001"))                                 // Size: 2 (total 11)
        }

        assertThat(compiledScript.scriptSizeInBytes).isEqualTo(11)
    }

    @Nested
    inner class ScriptObjectsTest {

        @Test
        fun `adding an object will add the object to the object list`() {
            val compiledScript = CompiledScript().apply {
                addObject("object1")
            }

            assertThat(compiledScript.objects.first()).isEqualTo("object1")
        }

        @Test
        fun `adding multiple objects will add the objects to the object list`() {
            val compiledScript = CompiledScript().apply {
                addObject("object1")
                addObject("object2")
            }

            assertThat(compiledScript.objects).isEqualTo(listOf("object1", "object2"))
        }

        @Test
        fun `adding an object that was already added doesn't do anything`() {
            val compiledScript = CompiledScript().apply {
                addObject("object1")
                addObject("object1")
            }

            assertThat(compiledScript.objects).isEqualTo(listOf("object1"))
        }
    }

    @Nested
    inner class ScriptGlobalsTest {

        @Test
        fun `adding a global will add the global to the globals list`() {
            val compiledScript = CompiledScript().apply {
                addGlobal("PLAYER_CHAR")
                addGlobal("PLAYER_ACTOR")
            }

            assertThat(compiledScript.globals).isEqualTo(listOf("PLAYER_CHAR", "PLAYER_ACTOR"))
        }

        @Test
        fun `adding the same global twice will only add it once`() {
            val compiledScript = CompiledScript().apply {
                addGlobal("PLAYER_CHAR")
                addGlobal("PLAYER_CHAR")
            }

            assertThat(compiledScript.globals).isEqualTo(listOf("PLAYER_CHAR"))
        }

        @Test
        fun `address for first global is calculated`() {
            val compiledScript = CompiledScript().apply {
                addGlobal("PLAYER_CHAR")
            }

            assertThat(compiledScript.getAddressForGlobal("PLAYER_CHAR"))
                .isEqualTo(0)
        }

        @Test
        fun `address for global is global index times 4`() {
            val compiledScript = CompiledScript().apply {
                addGlobal("PLAYER_CHAR")
                addGlobal("PLAYER_ACTOR")
                addGlobal("VEHICLE")
            }

            assertThat(compiledScript.getAddressForGlobal("PLAYER_ACTOR"))
                .isEqualTo(4)

            assertThat(compiledScript.getAddressForGlobal("VEHICLE"))
                .isEqualTo(8)
        }

        @Test
        fun `global references in script are added to globals`() {
            val compiledScript = CompiledScript().apply {
                addLine(OpcodeLine("0001", params = listOf(GlobalVar("PLAYER_CHAR"), GlobalVar("VEHICLE"))))
                addLine(OpcodeLine("0002", params = listOf(GlobalVar("PLAYER_ACTOR"), GlobalVar("PLAYER_CHAR"))))
            }

            assertThat(compiledScript.globals)
                .isEqualTo(listOf("PLAYER_CHAR", "VEHICLE", "PLAYER_ACTOR"))
        }
    }

    @Test
    fun `header size is calculated`() {
        val compiledScript = CompiledScript().apply {
            // Add some objects, this should increase the header size by 48
            addObject("object1")
            addObject("object2")
        }

        // Header Size = 64 (default header size) + 48 (object size) = 112
        assertThat(compiledScript.headerSize).isEqualTo(112)
    }
}