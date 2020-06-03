package nl.shadowlink.mission.msc.gamedata

abstract class GameData {

    protected abstract val models: Map<String, Int>

    fun getIdForModel(modelName: String): Int {
        val key = models.filterKeys { key -> key.equals(modelName, ignoreCase = true) }
            .keys.firstOrNull()

        return models[key] ?: throw IllegalStateException("Can't find ID for model [$modelName]")
    }
}