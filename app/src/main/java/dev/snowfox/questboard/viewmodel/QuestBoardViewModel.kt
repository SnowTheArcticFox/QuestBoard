package dev.snowfox.questboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.snowfox.questboard.data.repository.QuestRepository
import dev.snowfox.questboard.logic.LevelCalculator
import dev.snowfox.questboard.model.AppData
import dev.snowfox.questboard.model.LevelInfo
import dev.snowfox.questboard.model.Quest
import dev.snowfox.questboard.model.QuestSettings
import dev.snowfox.questboard.model.QuestTemplate
import dev.snowfox.questboard.model.QuestType
import dev.snowfox.questboard.notifications.QuestNotificationManager
import dev.snowfox.questboard.work.RolloverScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuestBoardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuestRepository(application)

    private val appData: StateFlow<AppData> = repository.appDataFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppData()
    )

    val activeQuests: StateFlow<List<Quest>> = appData.map { it.activeQuests }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val templates: StateFlow<List<QuestTemplate>> = appData.map { it.templates }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val settings: StateFlow<QuestSettings> = appData.map { it.settings }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = QuestSettings()
    )

    val levelInfo: StateFlow<LevelInfo> = appData.map { LevelCalculator.levelInfo(it.totalXp) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LevelCalculator.levelInfo(0)
    )

    init {
        // La notificación del tablero se re-dibuja sola cada vez que
        // cambian las misiones activas: marcar/desmarcar, sorteo nuevo,
        // cambio de cantidad en Ajustes, lo que sea.
        viewModelScope.launch {
            activeQuests.collect { quests ->
                QuestNotificationManager.updateBoardNotification(getApplication(), quests)
            }
        }

        viewModelScope.launch {
            // Revisa si toca sorteo al abrir la app...
            val didRoll = repository.rollIfNeeded()
            if (didRoll) {
                QuestNotificationManager.showReloadNotification(getApplication())
            }
            // ...y programa (o reprograma) el sorteo automático en segundo
            // plano para la próxima hora de reset.
            val currentSettings = repository.appDataFlow.first().settings
            RolloverScheduler.scheduleNext(getApplication(), currentSettings.resetHour)
        }
    }

    fun toggleQuest(quest: Quest) {
        viewModelScope.launch {
            repository.toggleQuest(quest.id)
        }
    }

    fun addTemplate(title: String, type: QuestType, xp: Int) {
        viewModelScope.launch {
            val newId = (templates.value.maxOfOrNull { it.id } ?: 0) + 1
            repository.addTemplate(QuestTemplate(id = newId, title = title, type = type, xp = xp))
        }
    }

    fun updateTemplate(template: QuestTemplate) {
        viewModelScope.launch {
            repository.updateTemplate(template)
        }
    }

    fun deleteTemplate(templateId: Int) {
        viewModelScope.launch {
            repository.deleteTemplate(templateId)
        }
    }

    fun updateResetHour(hour: Int) {
        viewModelScope.launch {
            val newSettings = settings.value.copy(resetHour = hour)
            repository.updateSettings(newSettings)
            RolloverScheduler.scheduleNext(getApplication(), hour)
        }
    }

    fun updateDailyCount(count: Int) {
        viewModelScope.launch {
            repository.updateSettings(settings.value.copy(dailyCount = count))
            repository.rerollType(QuestType.DAILY)
        }
    }

    fun updateWeeklyCount(count: Int) {
        viewModelScope.launch {
            repository.updateSettings(settings.value.copy(weeklyCount = count))
            repository.rerollType(QuestType.WEEKLY)
        }
    }

    fun updateMonthlyCount(count: Int) {
        viewModelScope.launch {
            repository.updateSettings(settings.value.copy(monthlyCount = count))
            repository.rerollType(QuestType.MONTHLY)
        }
    }

    // Temporal, para debugging: re-sortea los 3 tipos ya mismo.
    fun rerollNow() {
        viewModelScope.launch {
            repository.rerollAll()
            QuestNotificationManager.showReloadNotification(getApplication())
        }
    }
}