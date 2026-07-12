package dev.snowfox.questboard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.snowfox.questboard.ui.theme.QuestColors

// Controles pequeños reutilizados entre el formulario de misiones y
// la pantalla de configuración.

@Composable
fun FieldLabel(text: String) {
    Text(text = text, color = QuestColors.TextSecondary, fontSize = 12.sp)
    Spacer(Modifier.height(6.dp))
}

@Composable
fun StepperButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(1.dp, QuestColors.Divider, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = QuestColors.TextPrimary, fontSize = 18.sp)
    }
}