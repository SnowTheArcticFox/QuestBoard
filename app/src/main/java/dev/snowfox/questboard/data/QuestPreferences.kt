package dev.snowfox.questboard.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.questDataStore by preferencesDataStore(
    name = "quests"
)