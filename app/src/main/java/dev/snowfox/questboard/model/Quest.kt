package dev.snowfox.questboard.model

data class Quest (
    val title: String,
    val type: QuestType,
    val completed: Boolean = false
)

enum class QuestType {
    DAILY,
    WEEKLY,
    MONTHLY
}