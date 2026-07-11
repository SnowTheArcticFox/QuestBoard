package dev.snowfox.questboard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.snowfox.questboard.model.QuestType
import dev.snowfox.questboard.ui.components.QuestCard
import dev.snowfox.questboard.ui.components.QuestSectionTitle
import dev.snowfox.questboard.ui.theme.QuestColors
import dev.snowfox.questboard.viewmodel.QuestBoardViewModel

@Composable
fun QuestBoardScreen(viewModel: QuestBoardViewModel = viewModel()) {

    val quests by viewModel.quests.collectAsStateWithLifecycle()

    val dailyQuests = quests.filter { it.type == QuestType.DAILY }
    val weeklyQuests = quests.filter { it.type == QuestType.WEEKLY }
    val monthlyQuests = quests.filter { it.type == QuestType.MONTHLY }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(QuestColors.Background),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "QuestBoard",
                color = QuestColors.Accent,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            // Nota: acá va después el banner de nivel/XP, apenas el
            // ViewModel exponga ese estado (próximo paso).
            Spacer(Modifier.height(16.dp))
        }

        questSection("Daily Quests", dailyQuests, onToggle = viewModel::toggleQuest)
        questSection("Weekly Quests", weeklyQuests, onToggle = viewModel::toggleQuest)
        questSection("Monthly Quests", monthlyQuests, onToggle = viewModel::toggleQuest)
    }
}

// Agrega una sección completa (título + tarjetas) al LazyColumn.
// Al ser función de extensión de LazyListScope, evita repetir el
// mismo bloque de código 3 veces como en la versión anterior.
private fun androidx.compose.foundation.lazy.LazyListScope.questSection(
    title: String,
    quests: List<dev.snowfox.questboard.model.Quest>,
    onToggle: (dev.snowfox.questboard.model.Quest) -> Unit
) {
    if (quests.isEmpty()) return

    item { QuestSectionTitle(title) }

    items(quests, key = { it.id }) { quest ->
        QuestCard(quest = quest, onClick = { onToggle(quest) })
    }

    item { Spacer(Modifier.height(16.dp)) }
}