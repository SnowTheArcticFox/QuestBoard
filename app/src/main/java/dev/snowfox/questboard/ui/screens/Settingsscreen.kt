package dev.snowfox.questboard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.snowfox.questboard.ui.components.FieldLabel
import dev.snowfox.questboard.ui.components.QuestTopBar
import dev.snowfox.questboard.ui.components.StepperButton
import dev.snowfox.questboard.ui.theme.QuestColors
import dev.snowfox.questboard.viewmodel.QuestBoardViewModel

@Composable
fun SettingsScreen(viewModel: QuestBoardViewModel, onOpenDrawer: () -> Unit) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuestColors.Background)
            .padding(20.dp)
    ) {
        QuestTopBar(title = "Configuración", onOpenDrawer = onOpenDrawer)
        Spacer(Modifier.height(28.dp))

        FieldLabel("Hora de reinicio diario")
        Row(verticalAlignment = Alignment.CenterVertically) {
            StepperButton(text = "–") {
                viewModel.updateResetHour((settings.resetHour - 1 + 24) % 24)
            }
            Text(
                text = "%02d:00".format(settings.resetHour),
                color = QuestColors.Accent,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            StepperButton(text = "+") {
                viewModel.updateResetHour((settings.resetHour + 1) % 24)
            }
        }
        Text(
            text = "Las misiones diarias, semanales y mensuales se renuevan a esta hora, incluso si no abres la app.",
            color = QuestColors.TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(Modifier.height(28.dp))

        FieldLabel("Misiones diarias")
        CountRow(count = settings.dailyCount, onChange = viewModel::updateDailyCount)

        Spacer(Modifier.height(20.dp))

        FieldLabel("Misiones semanales")
        CountRow(count = settings.weeklyCount, onChange = viewModel::updateWeeklyCount)

        Spacer(Modifier.height(20.dp))

        FieldLabel("Misiones mensuales")
        CountRow(count = settings.monthlyCount, onChange = viewModel::updateMonthlyCount)

        Spacer(Modifier.height(40.dp))

        // TODO: quitar este botón antes de publicar la app — es solo
        // para probar el sorteo sin tener que esperar a la hora de reset.
        Button(
            onClick = { viewModel.rerollNow() },
            colors = ButtonDefaults.buttonColors(
                containerColor = QuestColors.Surface,
                contentColor = QuestColors.TextPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Re-sortear ahora (debug)")
        }
    }
}

@Composable
private fun CountRow(count: Int, onChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        StepperButton(text = "–") { if (count > 0) onChange(count - 1) }
        Text(
            text = "$count",
            color = QuestColors.Accent,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        StepperButton(text = "+") { onChange(count + 1) }
    }
}