package dev.snowfox.questboard.viewmodel

import androidx.lifecycle.ViewModel
import dev.snowfox.questboard.model.Quest
import dev.snowfox.questboard.data.sampleQuests
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuestBoardViewModel : ViewModel() {

    private val _quests = MutableStateFlow(sampleQuests)

    val quests = _quests.asStateFlow()


    fun toggleQuest(quest: Quest) {

        _quests.value = _quests.value.map {
            if (it.id == quest.id) {
                it.copy(
                    completed = !it.completed
                )
            } else {
                it
            }
        }

    }

}