package com.example.mob_dev_portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DailyLog_Dialog_ViewModel : ViewModel() {

    // Selected days (0 = Monday, 6 = Sunday)
    private val _selectedDays = MutableLiveData<Set<Int>>(setOf())
    val selectedDays: LiveData<Set<Int>> = _selectedDays

    // Physical sensations
    private val _selectedPhysicalSensations = MutableLiveData<Set<String>>(setOf())
    val selectedPhysicalSensations: LiveData<Set<String>> = _selectedPhysicalSensations

    // Mood
    private val _selectedMood = MutableLiveData<String>()
    val selectedMood: LiveData<String> = _selectedMood

    // Body areas with intensity (area name -> intensity level)
    private val _selectedBodyAreas = MutableLiveData<Map<String, Int>>(mapOf())
    val selectedBodyAreas: LiveData<Map<String, Int>> = _selectedBodyAreas

    // Treatment options
    private val _selectedTreatments = MutableLiveData<Set<String>>(setOf())
    val selectedTreatments: LiveData<Set<String>> = _selectedTreatments

    // Environment factors
    private val _selectedEnvironments = MutableLiveData<Set<String>>(setOf())
    val selectedEnvironments: LiveData<Set<String>> = _selectedEnvironments

    // Diet and hydration
    private val _selectedDietHydration = MutableLiveData<Set<String>>(setOf())
    val selectedDietHydration: LiveData<Set<String>> = _selectedDietHydration

    // Exercise and sleep
    private val _selectedExerciseSleep = MutableLiveData<Set<String>>(setOf())
    val selectedExerciseSleep: LiveData<Set<String>> = _selectedExerciseSleep

    // Toggle a day selection
    fun toggleDay(dayIndex: Int) {
        val currentDays = _selectedDays.value?.toMutableSet() ?: mutableSetOf()
        if (currentDays.contains(dayIndex)) {
            currentDays.remove(dayIndex)
        } else {
            currentDays.add(dayIndex)
        }
        _selectedDays.value = currentDays
    }

    // Select/deselect a physical sensation
    fun selectPhysicalSensation(sensation: String) {
        val currentSensations = _selectedPhysicalSensations.value?.toMutableSet() ?: mutableSetOf()
        if (currentSensations.contains(sensation)) {
            currentSensations.remove(sensation)
        } else {
            currentSensations.add(sensation)
        }
        _selectedPhysicalSensations.value = currentSensations
    }

    // Select a mood (single selection)
    fun selectMood(mood: String) {
        _selectedMood.value = mood
    }

    // Select/deselect a body area
    fun selectBodyArea(area: String, selected: Boolean, intensity: Int = 1) {
        val currentAreas = _selectedBodyAreas.value?.toMutableMap() ?: mutableMapOf()
        if (selected) {
            currentAreas[area] = intensity
        } else {
            currentAreas.remove(area)
        }
        _selectedBodyAreas.value = currentAreas
    }

    // Update intensity for a selected body area
    fun updateBodyAreaIntensity(area: String, intensity: Int) {
        val currentAreas = _selectedBodyAreas.value?.toMutableMap() ?: mutableMapOf()
        if (currentAreas.containsKey(area)) {
            currentAreas[area] = intensity
            _selectedBodyAreas.value = currentAreas
        }
    }

    // Select/deselect a treatment
    fun selectTreatment(treatment: String) {
        val currentTreatments = _selectedTreatments.value?.toMutableSet() ?: mutableSetOf()
        if (currentTreatments.contains(treatment)) {
            currentTreatments.remove(treatment)
        } else {
            currentTreatments.add(treatment)
        }
        _selectedTreatments.value = currentTreatments
    }

    // Select/deselect an environment factor
    fun selectEnvironment(environment: String) {
        val currentEnvironments = _selectedEnvironments.value?.toMutableSet() ?: mutableSetOf()
        if (currentEnvironments.contains(environment)) {
            currentEnvironments.remove(environment)
        } else {
            currentEnvironments.add(environment)
        }
        _selectedEnvironments.value = currentEnvironments
    }

    // Select/deselect a diet/hydration option
    fun selectDietHydration(option: String) {
        val currentOptions = _selectedDietHydration.value?.toMutableSet() ?: mutableSetOf()
        if (currentOptions.contains(option)) {
            currentOptions.remove(option)
        } else {
            currentOptions.add(option)
        }
        _selectedDietHydration.value = currentOptions
    }

    // Select/deselect an exercise/sleep option
    fun selectExerciseSleep(option: String) {
        val currentOptions = _selectedExerciseSleep.value?.toMutableSet() ?: mutableSetOf()
        if (currentOptions.contains(option)) {
            currentOptions.remove(option)
        } else {
            currentOptions.add(option)
        }
        _selectedExerciseSleep.value = currentOptions
    }

    // Save the complete log entry
    fun saveLogEntry(
        physicalSensations: String,
        mood: String,
        treatments: String,
        environments: String,
        dietHydration: String,
        exerciseSleep: String
    ) {
        // Here you would typically save to a database or repository
        // This is a placeholder for actual implementation
        // Example:
        // val logEntry = DailyLogEntry(
        //     date = getCurrentDate(),
        //     days = _selectedDays.value?.toList() ?: listOf(),
        //     sensations = physicalSensations,
        //     mood = mood,
        //     bodyAreas = _selectedBodyAreas.value ?: mapOf(),
        //     treatments = treatments,
        //     environments = environments,
        //     dietHydration = dietHydration,
        //     exerciseSleep = exerciseSleep
        // )
        // repository.saveDailyLogEntry(logEntry)
    }
}