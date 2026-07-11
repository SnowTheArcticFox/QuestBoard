package dev.snowfox.questboard.data

import dev.snowfox.questboard.model.Quest
import dev.snowfox.questboard.model.QuestType

val sampleQuests = listOf(
    Quest(
        id = 1,
        title = "Brush Teeth",
        type = QuestType.DAILY
    ),
    Quest(
        id = 2,
        title = "Open the curtains",
        type = QuestType.DAILY
    ),
    Quest(
        id = 3,
        title = "Open the windows",
        type = QuestType.DAILY
    ),
    Quest(
        id = 4,
        title = "Wash dishes",
        type = QuestType.WEEKLY
    ),
    Quest(
        id = 5,
        title = "Clean whole room",
        type = QuestType.MONTHLY
    )
)