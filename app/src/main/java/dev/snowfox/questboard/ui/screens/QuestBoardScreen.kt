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
import dev.snowfox.questboard.data.sampleQuests
import dev.snowfox.questboard.model.QuestType
import dev.snowfox.questboard.ui.components.QuestCard
import dev.snowfox.questboard.ui.components.QuestSectionTitle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue



@Composable
fun QuestBoardScreen() {

    var quests by remember {
        mutableStateOf(sampleQuests)
    }

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
                quests = quests.map {
                    if (it == quest) {
                        it.copy(
                            completed = !it.completed
                        )
                    } else {
                        it
                    }
                }
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
                quests = quests.map {
                    if (it == quest) {
                        it.copy(
                            completed = !it.completed
                        )
                    } else {
                        it
                    }
                }
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
                quests = quests.map {
                    if (it == quest) {
                        it.copy(
                            completed = !it.completed
                        )
                    } else {
                        it
                    }
                }
            })

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
    }
}