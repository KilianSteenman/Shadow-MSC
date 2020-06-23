package nl.shadowlink.mission.msc.compiler.scm

import com.google.common.truth.Truth.assertThat
import nl.shadowlink.mission.msc.compiler.CompiledScript
import nl.shadowlink.mission.msc.compiler.LabelLine
import nl.shadowlink.mission.msc.compiler.OpcodeLine
import nl.shadowlink.mission.msc.compiler.Script
import nl.shadowlink.mission.msc.fakes.FakeBinaryWriter
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ScmWriterTest {

    private val writer = ScmWriter()

    @Nested
    inner class HeaderTest {

        @Test
        fun `header size matches calculated header size`() {
            val script = Script().apply {
                addLine(LabelLine("Main"))
                addLine(OpcodeLine("0001"))

                addGlobal("PLAYER_CHAR")
                addGlobal("PLAYER_ACTOR")
            }

            val mission = Script().apply {
                addLine(OpcodeLine("0001"))
            }

            val compiledScript = CompiledScript().apply {
                main = script
                addMission(mission)
            }

            val bw = FakeBinaryWriter()
            writer.writeHeader(bw, compiledScript)

            assertThat(bw.writtenBytes.size).isEqualTo(compiledScript.headerSize)
        }
    }
}