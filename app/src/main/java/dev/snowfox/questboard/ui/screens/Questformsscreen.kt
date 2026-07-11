package dev.snowfox.questboard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.snowfox.questboard.model.QuestType
import dev.snowfox.questboard.ui.theme.QuestColors
import dev.snowfox.questboard.viewmodel.QuestBoardViewModel

@Composable
fun QuestFormScreen(
    viewModel: QuestBoardViewModel,
    questId: Int?,
    onDone: () -> Unit
) {
    val quests by viewModel.quests.collectAsStateWithLifecycle()
    val existingQuest = remember(questId, quests) { quests.find { it.id == questId } }
    val isEditing = existingQuest != null

    var title by remember(existingQuest) { mutableStateOf(existingQuest?.title ?: "") }
    var type by remember(existingQuest) { mutableStateOf(existingQuest?.type ?: QuestType.DAILY) }
    var xp by remember(existingQuest) { mutableStateOf(existingQuest?.xp ?: 10) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuestColors.Background)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "←",
                color = QuestColors.TextPrimary,
                fontSize = 22.sp,
                modifier = Modifier
                    .clickable(onClick = onDone)
                    .padding(end = 12.dp)
            )
            Text(
                text = if (isEditing) "Editar misión" else "Nueva misión",
                color = QuestColors.Accent,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(Modifier.height(28.dp))

        FieldLabel("Título")
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("Ej: Lavarte los dientes") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = QuestColors.TextPrimary,
                unfocusedTextColor = QuestColors.TextPrimary,
                focusedBorderColor = QuestColors.Accent,
                unfocusedBorderColor = QuestColors.Divider,
                cursorColor = QuestColors.Accent,
                focusedContainerColor = QuestColors.Surface,
                unfocusedContainerColor = QuestColors.Surface,
            )
        )

        Spacer(Modifier.height(20.dp))

        FieldLabel("Frecuencia")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            QuestType.values().forEach { option ->
                FilterChip(
                    selected = type == option,
                    onClick = { type = option },
                    label = { Text(etiquetaTipo(option)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = QuestColors.Accent,
                        selectedLabelColor = QuestColors.Background,
                        containerColor = QuestColors.Surface,
                        labelColor = QuestColors.TextSecondary
                    )
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        FieldLabel("Experiencia (XP)")
        Row(verticalAlignment = Alignment.CenterVertically) {
            StepperButton(text = "–", onClick = { if (xp > 5) xp -= 5 })
            Text(
                text = "$xp XP",
                color = QuestColors.Accent,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            StepperButton(text = "+", onClick = { xp += 5 })
        }

        Spacer(Modifier.height(36.dp))

        Button(
            onClick = {
                if (title.isNotBlank()) {
                    if (isEditing) {
                        viewModel.updateQuest(existingQuest!!.copy(title = title, type = type, xp = xp))
                    } else {
                        viewModel.addQuest(title = title, type = type, xp = xp)
                    }
                    onDone()
                }
            },
            enabled = title.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = QuestColors.Accent,
                contentColor = QuestColors.Background
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isEditing) "Guardar cambios" else "Crear misión")
        }

        if (isEditing) {
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = {
                    viewModel.deleteQuest(existingQuest!!.id)
                    onDone()
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = QuestColors.Danger),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar misión")
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(text = text, color = QuestColors.TextSecondary, fontSize = 12.sp)
    Spacer(Modifier.height(6.dp))
}

private fun etiquetaTipo(type: QuestType): String = when (type) {
    QuestType.DAILY -> "Diaria"
    QuestType.WEEKLY -> "Semanal"
    QuestType.MONTHLY -> "Mensual"
}

@Composable
private fun StepperButton(text: String, onClick: () -> Unit) {
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