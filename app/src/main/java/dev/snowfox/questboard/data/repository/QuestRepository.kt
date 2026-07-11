package dev.snowfox.questboard.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.snowfox.questboard.data.questDataStore
import dev.snowfox.questboard.data.sampleQuests
import dev.snowfox.questboard.model.Quest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val QUESTS_KEY = stringPreferencesKey("quests_json")

class QuestRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    // Flow reactivo: cualquier cambio en el DataStore se refleja acá solo,
    // no hay que llamar a nada manualmente para "refrescar".
    val questsFlow: Flow<List<Quest>> = context.questDataStore.data.map { prefs ->
        val raw = prefs[QUESTS_KEY]
        if (raw.isNullOrBlank()) {
            sampleQuests // primera vez que se abre la app: datos de ejemplo
        } else {
            json.decodeFromString(raw)
        }
    }

    suspend fun saveQuests(quests: List<Quest>) {
        context.questDataStore.edit { prefs ->
            prefs[QUESTS_KEY] = json.encodeToString(quests)
        }
    }

    suspend fun toggleQuest(questId: Int) {
        val current = questsFlow.first()
        val updated = current.map {
            if (it.id == questId) it.copy(completed = !it.completed) else it
        }
        saveQuests(updated)
    }

    // Útil más adelante para el botón de "exportar": es literalmente
    // el string que vas a escribir en el archivo que el usuario comparta.
    suspend fun exportAsJson(): String {
        return json.encodeToString(questsFlow.first())
    }

    suspend fun importFromJson(rawJson: String) {
        val imported: List<Quest> = json.decodeFromString(rawJson)
        saveQuests(imported)
    }
}