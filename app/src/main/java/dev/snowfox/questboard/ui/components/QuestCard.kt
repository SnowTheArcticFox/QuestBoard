package dev.snowfox.questboard.ui.components

import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.snowfox.questboard.model.Quest

@Composable
fun QuestCard(quest : Quest, onClick: () -> Unit){
    Card(
        onClick = onClick
    ) {
        Text(
            text = quest.title
        )

        Text(
            text = if (quest.completed) {
                "☑ ${quest.title}"
            } else {
                "☐ ${quest.title}"
            }
        )
    }
}