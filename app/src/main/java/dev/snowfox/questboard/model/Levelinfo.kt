package dev.snowfox.questboard.model

// No se persiste — se calcula al vuelo a partir de totalXp cada vez
// que se necesita mostrar.
data class LevelInfo(
    val level: Int,
    val xpIntoLevel: Int,
    val xpForNextLevel: Int
)