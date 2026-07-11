package dev.snowfox.questboard

data class Quest (
    val title: String,
    val type: QuestType
)

enum class QuestType {
    DAILY,
    WEEKLY,
    MONTHLY
}