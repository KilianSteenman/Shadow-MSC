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
}