package nl.shadowlink.mission.msc

import com.google.common.truth.Truth.assertThat
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
        }

        assertThat(compiledScript.scriptSizeInBytes).isEqualTo(8)
    }
}