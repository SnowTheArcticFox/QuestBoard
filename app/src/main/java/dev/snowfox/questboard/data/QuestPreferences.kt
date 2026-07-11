package dev.snowfox.questboard.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map


val Context.questDataStore by preferencesDataStore(
    name = "quests"
)


val COMPLETED_QUESTS =
    stringSetPreferencesKey("completed_quests")

fun Context.completedQuestIds(): Flow<Set<String>> {

    return questDataStore.data
        .catch {
            emit(emptyPreferences())
        }
        .let { preferences ->
            preferences.map {
                it[COMPLETED_QUESTS] ?: emptySet()
            }
        }
}