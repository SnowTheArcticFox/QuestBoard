package dev.snowfox.questboard.model

import kotlinx.serialization.Serializable

// Todo lo que la app persiste, en un solo objeto — un solo blob JSON en
// DataStore, igual que antes, solo que ahora con más partes adentro.
@Serializable
data class AppData(
    val templates: List<QuestTemplate> = emptyList(),
    val activeQuests: List<Quest> = emptyList(),
    val settings: QuestSettings = QuestSettings(),
    val rollover: RolloverState = RolloverState(),
    val totalXp: Int = 0
)