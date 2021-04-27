package nl.shadowlink.mission.msc.compiler

sealed class CompilationState {

    data class ScmCompilationSuccess(
        val globalCount: Int,
        val missionCount: Int,
        val mainSize: Int,
        val largestMissionSize: Int,
        val totalScriptSize: Int,
    ) : CompilationState()

    data class CleoCompilationSuccess(
        val scriptSize: Int
    ) : CompilationState()

    data class CompilationFailed(
        val error: String
    ) : CompilationState()
}