package dev.snowfox.questboard.model

import kotlinx.serialization.Serializable

// Una entrada del "pool": lo que el usuario crea/edita.
// No tiene estado de completado — eso vive en Quest (la misión ya asignada).
@Serializable
data class QuestTemplate(
    val id: Int,
    val title: String,
    val type: QuestType,
    val xp: Int = 10
)