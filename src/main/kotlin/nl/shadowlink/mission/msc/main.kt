package nl.shadowlink.mission.msc

import com.xenomachina.argparser.ArgParser
import nl.shadowlink.mission.msc.compiler.CompilationState
import nl.shadowlink.mission.msc.compiler.Compiler

fun main(args: Array<String>) {
    ArgParser(args).parseInto(::DefaultCompilerArgs).run {
        when (val compilationState = Compiler().compile(this)) {
            is CompilationState.ScmCompilationSuccess -> compilationState.print()
            is CompilationState.CleoCompilationSuccess -> compilationState.print()
            is CompilationState.CompilationFailed -> compilationState.print()
        }
    }
}

private fun CompilationState.ScmCompilationSuccess.print() {
    println(
        "Compilation finished\n" +
                "Globals: $globalCount\n" +
                "Missions: $missionCount\n" +
                "Main size: $mainSize bytes\n" +
                "Largest mission size: $largestMissionSize bytes\n" +
                "Total script size: $totalScriptSize bytes\n"
    )
}

private fun CompilationState.CleoCompilationSuccess.print() {
    println(
        "Cleo compilation finished\n" +
                "Script size: $scriptSize\n"
    )
}

private fun CompilationState.CompilationFailed.print() {
    println(
        "Compilation failed\n" +
                "Error: $error\n"
    )
}
