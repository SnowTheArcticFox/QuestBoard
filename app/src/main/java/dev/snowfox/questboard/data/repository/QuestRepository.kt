package dev.snowfox.questboard.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.snowfox.questboard.data.questDataStore
import dev.snowfox.questboard.data.sampleTemplates
import dev.snowfox.questboard.logic.QuestRolloverEngine
import dev.snowfox.questboard.model.AppData
import dev.snowfox.questboard.model.QuestSettings
import dev.snowfox.questboard.model.QuestTemplate
import dev.snowfox.questboard.model.QuestType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val APP_DATA_KEY = stringPreferencesKey("app_data_json")

class QuestRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    val appDataFlow: Flow<AppData> = context.questDataStore.data.map { prefs ->
        val raw = prefs[APP_DATA_KEY]
        if (raw.isNullOrBlank()) {
            AppData(templates = sampleTemplates)
        } else {
            json.decodeFromString(raw)
        }
    }

    private suspend fun current(): AppData = appDataFlow.first()

    suspend fun saveAppData(data: AppData) {
        context.questDataStore.edit { prefs ->
            prefs[APP_DATA_KEY] = json.encodeToString(data)
        }
    }

    // Revisa si toca sortear misiones nuevas (diarias/semanales/mensuales)
    // según la hora de reinicio configurada, y las sortea si corresponde.
    // Devuelve true si de verdad hubo un sorteo nuevo (para poder avisar).
    suspend fun rollIfNeeded(): Boolean {
        val data = current()
        val (newActive, newRollover) = QuestRolloverEngine.rollIfNeeded(
            templates = data.templates,
            active = data.activeQuests,
            settings = data.settings,
            rollover = data.rollover
        )
        val changed = newActive != data.activeQuests || newRollover != data.rollover
        if (changed) {
            saveAppData(data.copy(activeQuests = newActive, rollover = newRollover))
        }
        return changed
    }

    // Re-sortea un solo tipo (diaria/semanal/mensual) usando la cantidad
    // actual configurada. Se usa cuando cambias esa cantidad en Ajustes,
    // para que el cambio se note al instante en vez de esperar al próximo
    // ciclo automático.
    suspend fun rerollType(type: QuestType) {
        val data = current()
        val count = when (type) {
            QuestType.DAILY -> data.settings.dailyCount
            QuestType.WEEKLY -> data.settings.weeklyCount
            QuestType.MONTHLY -> data.settings.monthlyCount
        }
        val fresh = QuestRolloverEngine.draw(data.templates, type, count)
        saveAppData(data.copy(activeQuests = data.activeQuests.filterNot { it.type == type } + fresh))
    }

    // Re-sortea los 3 tipos de una. Pensado para un botón de debug.
    suspend fun rerollAll() {
        val data = current()
        val fresh = QuestType.entries.flatMap { type ->
            val count = when (type) {
                QuestType.DAILY -> data.settings.dailyCount
                QuestType.WEEKLY -> data.settings.weeklyCount
                QuestType.MONTHLY -> data.settings.monthlyCount
            }
            QuestRolloverEngine.draw(data.templates, type, count)
        }
        saveAppData(data.copy(activeQuests = fresh))
    }

    suspend fun toggleQuest(questId: Int) {
        val data = current()
        val quest = data.activeQuests.find { it.id == questId } ?: return

        val updatedQuests = data.activeQuests.map {
            if (it.id == questId) it.copy(completed = !it.completed) else it
        }

        // Si estaba completada, la estamos desmarcando -> se resta el XP.
        // Si no estaba completada, la estamos completando -> se suma.
        val xpDelta = if (quest.completed) -quest.xp else quest.xp
        val newTotalXp = (data.totalXp + xpDelta).coerceAtLeast(0)

        saveAppData(data.copy(activeQuests = updatedQuests, totalXp = newTotalXp))
    }

    suspend fun addTemplate(template: QuestTemplate) {
        val data = current()
        saveAppData(data.copy(templates = data.templates + template))
    }

    suspend fun updateTemplate(template: QuestTemplate) {
        val data = current()
        saveAppData(data.copy(templates = data.templates.map { if (it.id == template.id) template else it }))
    }

    suspend fun deleteTemplate(templateId: Int) {
        val data = current()
        // Nota: esto NO borra misiones ya activas hoy que vinieron de esta
        // plantilla, para no hacerte perder XP de algo que ya completaste.
        saveAppData(data.copy(templates = data.templates.filterNot { it.id == templateId }))
    }

    suspend fun updateSettings(settings: QuestSettings) {
        val data = current()
        saveAppData(data.copy(settings = settings))
    }

    suspend fun exportAsJson(): String = json.encodeToString(current())

    suspend fun importFromJson(rawJson: String) {
        val imported: AppData = json.decodeFromString(rawJson)
        saveAppData(imported)
    }
}