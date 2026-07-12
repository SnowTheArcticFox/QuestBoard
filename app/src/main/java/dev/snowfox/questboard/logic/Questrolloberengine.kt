package dev.snowfox.questboard.logic

import dev.snowfox.questboard.model.Quest
import dev.snowfox.questboard.model.QuestSettings
import dev.snowfox.questboard.model.QuestTemplate
import dev.snowfox.questboard.model.QuestType
import dev.snowfox.questboard.model.RolloverState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.IsoFields

// Decide si hay que sortear nuevas misiones diarias/semanales/mensuales,
// y las sortea. No toca Android ni DataStore — solo recibe datos y
// devuelve datos, así es fácil de probar y de razonar.
object QuestRolloverEngine {

    // La "fecha lógica": si son las 2am y tu resetHour es 4, todavía
    // cuenta como el día de ayer. Así "hoy" no cambia hasta tu hora custom.
    fun logicalDate(now: LocalDateTime, resetHour: Int): LocalDate {
        return if (now.hour < resetHour) now.toLocalDate().minusDays(1) else now.toLocalDate()
    }

    private fun weekKey(date: LocalDate): String {
        val week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
        val year = date.get(IsoFields.WEEK_BASED_YEAR)
        return "$year-W$week"
    }

    private fun monthKey(date: LocalDate): String {
        return "%04d-%02d".format(date.year, date.monthValue)
    }

    fun rollIfNeeded(
        templates: List<QuestTemplate>,
        active: List<Quest>,
        settings: QuestSettings,
        rollover: RolloverState,
        now: LocalDateTime = LocalDateTime.now()
    ): Pair<List<Quest>, RolloverState> {
        val today = logicalDate(now, settings.resetHour)
        val todayEpoch = today.toEpochDay()
        val thisWeekKey = weekKey(today)
        val thisMonthKey = monthKey(today)

        var quests = active
        var newRollover = rollover

        if (rollover.lastDailyLogicalDateEpochDay != todayEpoch) {
            quests = replaceType(quests, QuestType.DAILY, draw(templates, QuestType.DAILY, settings.dailyCount))
            newRollover = newRollover.copy(lastDailyLogicalDateEpochDay = todayEpoch)
        }
        if (rollover.lastWeeklyLogicalWeekKey != thisWeekKey) {
            quests = replaceType(quests, QuestType.WEEKLY, draw(templates, QuestType.WEEKLY, settings.weeklyCount))
            newRollover = newRollover.copy(lastWeeklyLogicalWeekKey = thisWeekKey)
        }
        if (rollover.lastMonthlyLogicalMonthKey != thisMonthKey) {
            quests = replaceType(quests, QuestType.MONTHLY, draw(templates, QuestType.MONTHLY, settings.monthlyCount))
            newRollover = newRollover.copy(lastMonthlyLogicalMonthKey = thisMonthKey)
        }

        return quests to newRollover
    }

    // Quita las misiones activas de este tipo y pone las nuevas sorteadas.
    private fun replaceType(current: List<Quest>, type: QuestType, freshlyDrawn: List<Quest>): List<Quest> {
        return current.filterNot { it.type == type } + freshlyDrawn
    }

    fun draw(templates: List<QuestTemplate>, type: QuestType, count: Int): List<Quest> {
        val pool = templates.filter { it.type == type }
        if (pool.isEmpty() || count <= 0) return emptyList()
        return pool.shuffled()
            .take(count.coerceAtMost(pool.size))
            .map { template ->
                Quest(id = template.id, title = template.title, type = template.type, xp = template.xp)
            }
    }
}