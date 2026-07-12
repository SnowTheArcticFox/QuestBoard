package dev.snowfox.questboard.logic

import dev.snowfox.questboard.model.LevelInfo

// Curva de nivel: subir del nivel N al N+1 cuesta N*50 XP.
// Nv.1→2: 50 XP, Nv.2→3: 100 XP, Nv.3→4: 150 XP, etc.
// Para cambiar la dificultad de subir de nivel, solo hay que tocar
// xpRequiredForLevel — el resto se ajusta solo.
object LevelCalculator {

    fun xpRequiredForLevel(level: Int): Int = level * 50

    fun levelInfo(totalXp: Int): LevelInfo {
        var level = 1
        var remaining = totalXp
        while (remaining >= xpRequiredForLevel(level)) {
            remaining -= xpRequiredForLevel(level)
            level++
        }
        return LevelInfo(
            level = level,
            xpIntoLevel = remaining,
            xpForNextLevel = xpRequiredForLevel(level)
        )
    }
}