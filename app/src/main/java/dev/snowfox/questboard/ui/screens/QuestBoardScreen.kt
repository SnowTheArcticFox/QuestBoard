package dev.snowfox.questboard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.snowfox.questboard.model.Quest
import dev.snowfox.questboard.model.QuestType
import dev.snowfox.questboard.ui.components.LevelBanner
import dev.snowfox.questboard.ui.components.QuestCard
import dev.snowfox.questboard.ui.components.QuestSectionTitle
import dev.snowfox.questboard.ui.components.QuestTopBar
import dev.snowfox.questboard.ui.theme.QuestColors
import dev.snowfox.questboard.viewmodel.QuestBoardViewModel

@Composable
fun QuestBoardScreen(viewModel: QuestBoardViewModel, onOpenDrawer: () -> Unit) {
    val quests by viewModel.activeQuests.collectAsStateWithLifecycle()
    val levelInfo by viewModel.levelInfo.collectAsStateWithLifecycle()

    val dailyQuests = quests.filter { it.type == QuestType.DAILY }
    val weeklyQuests = quests.filter { it.type == QuestType.WEEKLY }
    val monthlyQuests = quests.filter { it.type == QuestType.MONTHLY }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QuestColors.Background)
            .padding(20.dp)
    ) {
        QuestTopBar(title = "Misiones de hoy", onOpenDrawer = onOpenDrawer)
        Spacer(Modifier.height(16.dp))
        LevelBanner(levelInfo = levelInfo)
        Spacer(Modifier.height(16.dp))

        if (quests.isEmpty()) {
            Text(
                text = "Todavía no hay misiones activas. Agrega algunas desde el Pool de misiones (☰).",
                color = QuestColors.TextSecondary,
                fontSize = 13.sp
            )
            return@Column
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            questSection("Daily Quests", dailyQuests, viewModel::toggleQuest)
            questSection("Weekly Quests", weeklyQuests, viewModel::toggleQuest)
            questSection("Monthly Quests", monthlyQuests, viewModel::toggleQuest)
        }
    }
}

private fun LazyListScope.questSection(title: String, quests: List<Quest>, onToggle: (Quest) -> Unit) {
    if (quests.isEmpty()) return
    item { QuestSectionTitle(title) }
    items(quests, key = { it.id }) { quest ->
        QuestCard(quest = quest, onClick = { onToggle(quest) })
    }
    item { Spacer(Modifier.height(16.dp)) }
}