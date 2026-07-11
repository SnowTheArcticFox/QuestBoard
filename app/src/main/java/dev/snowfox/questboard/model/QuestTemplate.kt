package dev.snowfox.questboard.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestTemplate(
    val id: Int,
    val title: String,
    val type: QuestType,
    val xp: Int = 10
)