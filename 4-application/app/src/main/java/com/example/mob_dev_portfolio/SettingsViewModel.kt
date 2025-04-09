package com.example.mob_dev_portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob_dev_portfolio.data.PrefRepo
import kotlinx.coroutines.launch

class SettingsViewModel(private val prefRepo: PrefRepo) : ViewModel() {
    private val _selectedLocation = MutableLiveData<String>()
    val selectedLocation: LiveData<String> get() = _selectedLocation
    private val _latitude = MutableLiveData<String>()
    val latitude: LiveData<String> get() = _latitude
    private val _longitude = MutableLiveData<String>()
    val longitude: LiveData<String> get() = _longitude
    private val _simulatedCondition = MutableLiveData<String>()
    val simulatedCondition: LiveData<String> get() = _simulatedCondition

    init {
        // current preferences from DataStore
        viewModelScope.launch {
            prefRepo.prefFlow.collect { prefs ->
                _selectedLocation.value = prefs.selectedLocation
                _latitude.value = prefs.latitude
                _longitude.value = prefs.longitude
                _simulatedCondition.value = prefs.simulateCondition
            }
        }
    }

    fun saveLocation(location: String) {
        viewModelScope.launch {
            // First determine the appropriate latitude and longitude for this location
            val (latitude, longitude) = getCoordinatesForLocation(location)

            // Save all values to the repository
            prefRepo.saveSimulatedLocation(location)
            prefRepo.saveLatitude(latitude)
            prefRepo.saveLongitude(longitude)

            // Update LiveData values
            _selectedLocation.value = location
            _latitude.value = latitude
            _longitude.value = longitude
        }
    }

    fun saveWeatherCondition(condition: String) {
        viewModelScope.launch {
            prefRepo.saveSimulatedCondition(condition)
            _simulatedCondition.value = condition
        }
    }

    // Helper func to get coordinates based on location name
    private fun getCoordinatesForLocation(location: String): Pair<String, String> {
        return when (location) {
            "cardiff" -> Pair("51.481583", "-3.179090")
            "dubai" -> Pair("25.276987", "55.296249")
            "singapore" -> Pair("1.352083", "103.819839")
            "reykjavik" -> Pair("64.128288", "-21.827774")
            else -> Pair("51.481583", "-3.179090")
        }
    }

}
