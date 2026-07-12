package dev.snowfox.questboard.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestSettings(
    val resetHour: Int = 4,       // 0-23. Hora del día en que se sortean nuevas misiones
    val dailyCount: Int = 5,
    val weeklyCount: Int = 3,
    val monthlyCount: Int = 1
)