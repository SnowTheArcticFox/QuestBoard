package dev.snowfox.questboard.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.snowfox.questboard.model.Quest
import dev.snowfox.questboard.ui.theme.QuestColors

@Composable
fun QuestCard(quest: Quest, onClick: () -> Unit, onEdit: () -> Unit) {
    val backgroundColor by animateColorAsState(
        targetValue = if (quest.completed) QuestColors.SurfaceDone else QuestColors.Surface,
        label = "questCardBackground"
    )

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            QuestSeal(completed = quest.completed)

            Spacer(Modifier.width(12.dp))

            Text(
                text = quest.title,
                color = if (quest.completed) QuestColors.TextSecondary else QuestColors.TextPrimary,
                textDecoration = if (quest.completed) TextDecoration.LineThrough else null,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = "+${quest.xp} XP",
                color = if (quest.completed) QuestColors.Success else QuestColors.Accent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.width(10.dp))

            // Ícono de editar. Al estar dentro del Card (que ya tiene su
            // propio onClick), este clickable "captura" el toque en su
            // propia área, así que no dispara el toggle de completado.
            Text(
                text = "✎",
                color = QuestColors.TextSecondary,
                fontSize = 15.sp,
                modifier = Modifier
                    .clickable(onClick = onEdit)
                    .padding(4.dp)
            )
        }
    }
}

// Círculo tipo "sello" que se rellena de verde con un check al completar
@Composable
private fun QuestSeal(completed: Boolean) {
    val borderColor by animateColorAsState(
        targetValue = if (completed) QuestColors.Success else QuestColors.Divider,
        label = "questSealBorder"
    )

    Box(
        modifier = Modifier
            .size(26.dp)
            .background(
                color = if (completed) QuestColors.Success else Color.Transparent,
                shape = CircleShape
            )
            .border(width = 2.dp, color = borderColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (completed) {
            Text(
                text = "✓",
                color = QuestColors.Background,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}