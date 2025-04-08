package com.example.mob_dev_portfolio.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class PrefRepo (private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val LAST_UPDATED = stringPreferencesKey("last_updated")
    }
    val prefFlow: Flow<Prefs> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }
    private fun mapUserPreferences(preferences: Preferences)
            : Prefs {
        val lastUpdate =
            preferences[PreferencesKeys.LAST_UPDATED] ?: "Never updated..."
        return Prefs(lastUpdate)
    }

    suspend fun updateLastUpdate(lastUpdate: LocalDateTime)
    {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_UPDATED] =
                lastUpdate.toString()
        }
    }


}

// Slides said to put inside file
data class Prefs(
    val lastUpdate: String,
){}