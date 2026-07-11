package dev.snowfox.questboard.data.repository

import android.content.Context
import dev.snowfox.questboard.data.questDataStore
import dev.snowfox.questboard.model.Quest
import dev.snowfox.questboard.data.sampleQuests

class QuestRepository {

    fun getQuests(): List<Quest> {
        return sampleQuests
    }

}