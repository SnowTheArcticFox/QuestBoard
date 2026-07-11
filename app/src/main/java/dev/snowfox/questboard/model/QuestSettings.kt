package dev.snowfox.questboard.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestSettings(

    val resetHour: Int = 4,

    val dailyCount: Int = 5,

    val weeklyCount: Int = 2,

    val monthlyCount: Int = 1

)