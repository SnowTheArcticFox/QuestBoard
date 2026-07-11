package dev.snowfox.questboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.snowfox.questboard.data.repository.QuestRepository
import dev.snowfox.questboard.model.Quest
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuestBoardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuestRepository(application)

    val quests: StateFlow<List<Quest>> = repository.questsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun toggleQuest(quest: Quest) {
        viewModelScope.launch {
            repository.toggleQuest(quest.id)
        }
    }
}