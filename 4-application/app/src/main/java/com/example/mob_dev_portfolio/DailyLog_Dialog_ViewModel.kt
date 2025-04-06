package com.example.mob_dev_portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DailyLog_Dialog_ViewModel : ViewModel() {
    // Generic function to toggle selections in a set
    private fun <T> toggleSelection(
        currentSelections: MutableSet<T>?,
        item: T
    ): MutableSet<T> {
        val selections = currentSelections?.toMutableSet() ?: mutableSetOf()
        if (selections.contains(item)) {
            selections.remove(item)
        } else {
            selections.add(item)
        }
        return selections
    }

    // Days selection
    private val _selectedDays = MutableLiveData<Set<Int>>(emptySet())
    val selectedDays: LiveData<Set<Int>> = _selectedDays

    // Physical sensations selection
    private val _selectedPhysicalSensations = MutableLiveData<MutableSet<String>>(mutableSetOf())
    val selectedPhysicalSensations: LiveData<MutableSet<String>> = _selectedPhysicalSensations

    // Mood selection
    private val _selectedMood = MutableLiveData<MutableSet<String>>(mutableSetOf())
    val selectedMood: LiveData<MutableSet<String>> = _selectedMood

    // Environment selection
    private val _selectedEnvironments = MutableLiveData<MutableSet<String>>(mutableSetOf())
    val selectedEnvironments: LiveData<MutableSet<String>> = _selectedEnvironments

    // Toggle day selection
    fun toggleDay(dayIndex: Int) {
        _selectedDays.value = toggleSelection(
            _selectedDays.value?.toMutableSet(),
            dayIndex
        )
    }

    // Select or deselect physical sensation
    fun selectPhysicalSensation(sensation: String) {
        _selectedPhysicalSensations.value = toggleSelection(_selectedPhysicalSensations.value, sensation)
    }

    // Select or deselect mood
    fun selectMood(mood: String) {
        _selectedMood.value = toggleSelection(_selectedMood.value, mood)
    }

    // Select or deselect environment
    fun selectEnvironment(environment: String) {
        _selectedEnvironments.value = toggleSelection(_selectedEnvironments.value, environment)
    }

    // Body areas selection with intensity
    data class BodyAreaSelection(
        val area: String,
        val intensity: Int = 1  // Default to moderate (1)
    )

    // Body areas selection
    private val _selectedBodyAreas = MutableLiveData<MutableMap<String, Int>>(mutableMapOf())
    val selectedBodyAreas: LiveData<MutableMap<String, Int>> = _selectedBodyAreas

    // Select or update body area with intensity
    fun selectBodyArea(area: String, selected: Boolean, intensity: Int = 1) {
        val currentSelections = _selectedBodyAreas.value ?: mutableMapOf()

        if (selected) {
            currentSelections[area] = intensity
        } else {
            currentSelections.remove(area)
        }

        _selectedBodyAreas.value = currentSelections
    }

    // Update intensity for a selected body area
    fun updateBodyAreaIntensity(area: String, intensity: Int) {
        val currentSelections = _selectedBodyAreas.value ?: mutableMapOf()

        if (currentSelections.containsKey(area)) {
            currentSelections[area] = intensity
            _selectedBodyAreas.value = currentSelections
        }
    }

    // Save log entry
    fun saveLogEntry(
        physicalSensations: String,
        mood: Int,
        treatment: String,
        environment: String,
        diet: String,
        other: String
    ) {
        // Implement your data saving logic here
        println("Saving Log Entry:")
        println("Physical Sensations: $physicalSensations")
        println("Mood: $mood")
        println("Treatment: $treatment")
        println("Environment: $environment")
        println("Diet: $diet")
        println("Other Notes: $other")
    }
}