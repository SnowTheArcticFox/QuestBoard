package dev.snowfox.questboard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.snowfox.questboard.model.Quest
import dev.snowfox.questboard.model.QuestType
import dev.snowfox.questboard.ui.components.QuestCard
import dev.snowfox.questboard.ui.components.QuestSectionTitle
import dev.snowfox.questboard.ui.theme.QuestColors
import dev.snowfox.questboard.viewmodel.QuestBoardViewModel

@Composable
fun QuestBoardScreen(
    viewModel: QuestBoardViewModel,
    onAddQuest: () -> Unit,
    onEditQuest: (Int) -> Unit
) {
    val quests by viewModel.quests.collectAsStateWithLifecycle()

    val dailyQuests = quests.filter { it.type == QuestType.DAILY }
    val weeklyQuests = quests.filter { it.type == QuestType.WEEKLY }
    val monthlyQuests = quests.filter { it.type == QuestType.MONTHLY }

    Scaffold(
        containerColor = QuestColors.Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddQuest,
                containerColor = QuestColors.Accent,
                contentColor = QuestColors.Background
            ) {
                Text(text = "+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(QuestColors.Background),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = padding.calculateBottomPadding() + 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "QuestBoard",
                    color = QuestColors.Accent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(Modifier.height(16.dp))
            }

            questSection("Daily Quests", dailyQuests, viewModel::toggleQuest, onEditQuest)
            questSection("Weekly Quests", weeklyQuests, viewModel::toggleQuest, onEditQuest)
            questSection("Monthly Quests", monthlyQuests, viewModel::toggleQuest, onEditQuest)
        }
    }
}

// Agrega una sección completa (título + tarjetas) al LazyColumn.
private fun LazyListScope.questSection(
    title: String,
    quests: List<Quest>,
    onToggle: (Quest) -> Unit,
    onEdit: (Int) -> Unit
) {
    if (quests.isEmpty()) return

    item { QuestSectionTitle(title) }

    items(quests, key = { it.id }) { quest ->
        QuestCard(
            quest = quest,
            onClick = { onToggle(quest) },
            onEdit = { onEdit(quest.id) }
        )
    }

    item { Spacer(Modifier.height(16.dp)) }
}