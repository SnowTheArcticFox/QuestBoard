package dev.snowfox.questboard.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta "bitácora de aventurero" — fondo tinta oscura + acento dorado
// para nivel/XP, verde para lo completado. Úsala en toda la app para
// mantener consistencia visual.
object QuestColors {
    val Background = Color(0xFF14171F)
    val Surface = Color(0xFF1E2230)
    val SurfaceDone = Color(0xFF1A2A22)
    val Accent = Color(0xFFE8B75E)      // dorado — nivel / XP
    val Success = Color(0xFF6FCF97)     // verde — misión completada
    val Danger = Color(0xFFE07A5F)      // coral — acciones destructivas (eliminar)
    val TextPrimary = Color(0xFFF0EDE4)
    val TextSecondary = Color(0xFF8B90A3)
    val Divider = Color(0xFF2C3142)
}