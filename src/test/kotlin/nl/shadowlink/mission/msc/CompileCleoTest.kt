package nl.shadowlink.mission.msc

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

class CompileCleoTest {

    @AfterEach
    fun after() {
        javaClass.getResource("/cleo_test.cs")?.file?.toFile()?.delete()
        javaClass.getResource("/cleo_output.cs")?.file?.toFile()?.delete()
    }

    @Test
    fun `cleo script is compiled to same dir with cs extension`() {
        val inputPath = File(javaClass.getResource("/cleo_test.dsc")!!.file).absolutePath

        main(arrayOf("--cleo-input", inputPath))

        assertTrue(File(inputPath.replaceAfter(".", "cs")).exists())
    }

    @Test
    fun `cleo script is created in output dir`() {
        val inputPath = File(javaClass.getResource("/cleo_test.dsc")!!.file).absolutePath
        val outputPath = inputPath.replace("cleo_test.dsc", "cleo_output.cs")

        main(arrayOf("--cleo-input", inputPath, "--cleo-output", outputPath))

        assertTrue(File(outputPath).exists())
    }

    companion object {

        private fun String?.toFile(): File? {
            if(this == null) return null

            return try {
                File(this)
            } catch(e: Exception) {
                null
            }
        }
    }
}