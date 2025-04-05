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