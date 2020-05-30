package nl.shadowlink.mission.msc

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CompiledScriptTest {

    @Test
    fun `label addresses is mapped`() {
        val compiledScript = CompiledScript()
        compiledScript.apply {
            addLine(LabelLine("INIT"))
        }

        assertThat(compiledScript.getAddressForLabel("INIT")).isEqualTo(0)
    }

    @Test
    fun `total script size is calculated`() {
        val compiledScript = CompiledScript()
        compiledScript.apply {
            addLine(LabelLine("INIT"))                                   // Size: 0 (total 0)
            addLine(OpcodeLine("0001"))                                 // Size: 2 (total 2)
            addLine(OpcodeLine("0002", listOf(IntParam(0))))      // Size: 6 (total 8)
            addLine(LabelLine("INIT_2"))                                  // Size: 0 (total 8)
            addLine(OpcodeLine("0001"))                                 // Size: 2 (total 10)
        }

        assertThat(compiledScript.scriptSizeInBytes).isEqualTo(10)
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