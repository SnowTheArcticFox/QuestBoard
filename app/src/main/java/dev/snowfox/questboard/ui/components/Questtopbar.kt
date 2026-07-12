package dev.snowfox.questboard.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.snowfox.questboard.ui.theme.QuestColors

// Barra superior simple con botón de hamburguesa para abrir el drawer.
// Se usa igual en las 3 pantallas principales (tablero, pool, config).
@Composable
fun QuestTopBar(title: String, onOpenDrawer: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "☰",
            color = QuestColors.TextPrimary,
            fontSize = 22.sp,
            modifier = Modifier
                .clickable(onClick = onOpenDrawer)
                .padding(end = 14.dp)
        )
        Text(
            text = title,
            color = QuestColors.Accent,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
    }
}