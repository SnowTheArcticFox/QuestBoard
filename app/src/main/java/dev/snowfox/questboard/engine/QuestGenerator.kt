package dev.snowfox.questboard.engine

import dev.snowfox.questboard.model.ActiveQuest
import dev.snowfox.questboard.model.QuestSettings
import dev.snowfox.questboard.model.QuestTemplate
import dev.snowfox.questboard.model.QuestType

object QuestGenerator {

    fun generate(
        templates: List<QuestTemplate>,
        settings: QuestSettings
    ): List<ActiveQuest> {

        val result = mutableListOf<ActiveQuest>()

        result += pick(templates, QuestType.DAILY, settings.dailyCount)
        result += pick(templates, QuestType.WEEKLY, settings.weeklyCount)
        result += pick(templates, QuestType.MONTHLY, settings.monthlyCount)

        return result
    }

    private fun pick(
        templates: List<QuestTemplate>,
        type: QuestType,
        count: Int
    ): List<ActiveQuest> {

        return templates
            .filter { it.type == type }
            .shuffled()
            .take(count)
            .map {
                ActiveQuest(
                    templateId = it.id,
                    title = it.title,
                    type = it.type,
                    xp = it.xp
                )
            }
    }
}