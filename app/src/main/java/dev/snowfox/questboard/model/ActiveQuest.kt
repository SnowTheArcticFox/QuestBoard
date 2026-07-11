package dev.snowfox.questboard.model

import kotlinx.serialization.Serializable

@Serializable
data class ActiveQuest(
    val templateId: Int,
    val title: String,
    val type: QuestType,
    val xp: Int,
    val completed: Boolean = false
)