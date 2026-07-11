package dev.snowfox.questboard.model

import kotlinx.serialization.Serializable

@Serializable
data class Quest(
    val id: Int,
    val title: String,
    val type: QuestType,
    val xp: Int = 10,
    val completed: Boolean = false
)

@Serializable
enum class QuestType {
    DAILY,
    WEEKLY,
    MONTHLY
}