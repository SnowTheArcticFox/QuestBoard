package dev.snowfox.questboard.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.snowfox.questboard.model.QuestType
import dev.snowfox.questboard.ui.components.QuestCard
import dev.snowfox.questboard.ui.components.QuestSectionTitle

import androidx.compose.runtime.getValue

import androidx.lifecycle.viewmodel.compose.viewModel
import dev.snowfox.questboard.viewmodel.QuestBoardViewModel

import androidx.lifecycle.compose.collectAsStateWithLifecycle



@Composable
fun QuestBoardScreen(viewModel: QuestBoardViewModel = viewModel()) {

    val quests by viewModel.quests.collectAsStateWithLifecycle()

    val dailyQuests = quests.filter {
        it.type == QuestType.DAILY
    }

    val weeklyQuests = quests.filter {
        it.type == QuestType.WEEKLY
    }

    val monthlyQuests = quests.filter {
        it.type == QuestType.MONTHLY
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "QuestBoard!"
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        QuestSectionTitle("Daily Quests")

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        dailyQuests.forEach {quest ->
            QuestCard(quest = quest, onClick = {
                viewModel.toggleQuest(quest)
            })

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        QuestSectionTitle("Weekly Quests")

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        weeklyQuests.forEach {quest ->
            QuestCard(quest = quest, onClick = {
                viewModel.toggleQuest(quest)
            })

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        QuestSectionTitle("Monthly Quests")

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        monthlyQuests.forEach {quest ->
            QuestCard(quest = quest, onClick = {
                viewModel.toggleQuest(quest)
            })

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
    }
}