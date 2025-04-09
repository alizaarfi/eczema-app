package com.example.mob_dev_portfolio.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import com.example.mob_dev_portfolio.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class PrefRepo (private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val LAST_UPDATED = stringPreferencesKey("last_updated")
        val LONGITUDE = stringPreferencesKey("longitude")
        val LATITUDE = stringPreferencesKey("latitude")
        val SELECTED_LOCATION = stringPreferencesKey("selected_location")
        val SIMULATED_CONDITION = stringPreferencesKey("simulate_condition")
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
    private fun mapUserPreferences(preferences: Preferences): Prefs {
        // Set a default for each as well
        val lastUpdate = preferences[PreferencesKeys.LAST_UPDATED] ?: "Never updated..."
        val longitude = preferences[PreferencesKeys.LONGITUDE] ?: "-3.179090"
        val latitude = preferences[PreferencesKeys.LATITUDE] ?: "51.481583"
        val selectedLocation = preferences[PreferencesKeys.SELECTED_LOCATION] ?: "Cardiff"
        val simulateCondition = preferences[PreferencesKeys.SIMULATED_CONDITION] ?: "hot_dry"

        return Prefs(lastUpdate, longitude, latitude, selectedLocation, simulateCondition)
    }

//    suspend fun updateLastUpdate(lastUpdate: LocalDateTime)
//    {
//        dataStore.edit { preferences ->
//            preferences[PreferencesKeys.LAST_UPDATED] =
//                lastUpdate.toString()
//        }
//    }

    suspend fun saveSimulatedLocation(location: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_LOCATION] = location
        }
    }

    suspend fun saveSimulatedCondition(condition: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SIMULATED_CONDITION] = condition
        }
    }

    suspend fun saveLatitude(latitude: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LATITUDE] = latitude
        }
    }

    suspend fun saveLongitude(longitude: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LONGITUDE] = longitude
        }
    }


}

// Slides said to put inside file
data class Prefs(
    val lastUpdate: String,
    val longitude: String,
    val latitude: String,
    val selectedLocation: String,
    val simulateCondition: String
){}