package dev.snowfox.questboard.model

import kotlinx.serialization.Serializable

@Serializable
data class AppData(

    val templates: List<QuestTemplate> = emptyList(),

    val activeQuests: List<ActiveQuest> = emptyList(),

    val settings: QuestSettings = QuestSettings(),

    val rollover: RolloverState = RolloverState()

)