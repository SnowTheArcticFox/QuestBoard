package dev.snowfox.questboard.model

import kotlinx.serialization.Serializable

// Guarda cuándo fue el último sorteo de cada tipo, para saber si ya toca
// uno nuevo. Usamos claves de "día lógico" (ajustado por resetHour), no
// la fecha de calendario cruda.
@Serializable
data class RolloverState(
    val lastDailyLogicalDateEpochDay: Long? = null,
    val lastWeeklyLogicalWeekKey: String? = null,   // ej: "2026-W28"
    val lastMonthlyLogicalMonthKey: String? = null  // ej: "2026-07"
)