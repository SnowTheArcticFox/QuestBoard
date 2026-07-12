package dev.snowfox.questboard.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.snowfox.questboard.model.QuestTemplate
import dev.snowfox.questboard.ui.theme.QuestColors

// Tarjeta para una entrada del pool (plantilla). A diferencia de
// QuestCard, no tiene estado de completado — tocarla abre el formulario
// para editarla.
@Composable
fun TemplateCard(template: QuestTemplate, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = QuestColors.Surface)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Text(
                text = template.title,
                color = QuestColors.TextPrimary,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "+${template.xp} XP",
                color = QuestColors.Accent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}