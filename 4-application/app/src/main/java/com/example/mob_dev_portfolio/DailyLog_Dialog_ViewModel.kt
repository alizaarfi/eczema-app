package com.example.mob_dev_portfolio

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Locale

class DailyLog_Dialog_ViewModel : ViewModel() {

    // Selected day (0 = Monday, 6 = Sunday)
    private val _selectedDay = MutableLiveData<Int>()
    val selectedDay: LiveData<Int> = _selectedDay

    // Current week offset (0 = current week, -1 = last week, etc.)
    private val _currentWeekOffset = MutableLiveData<Int>(0)
    val currentWeekOffset: LiveData<Int> = _currentWeekOffset

    // Today's day of the week (0 = Monday, 6 = Sunday)
    private val _todayDayOfWeek = MutableLiveData<Int>()
    val todayDayOfWeek: LiveData<Int> = _todayDayOfWeek

    // Current date display
    private val _currentDateDisplay = MutableLiveData<String>()
    val currentDateDisplay: LiveData<String> = _currentDateDisplay

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

    // Initialize with current day
    init {
        val calendar = Calendar.getInstance()
        // Convert to our day index (0 = Monday, 6 = Sunday)
        val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> 6
            else -> calendar.get(Calendar.DAY_OF_WEEK) - 2
        }
        _todayDayOfWeek.value = dayOfWeek
        _selectedDay.value = dayOfWeek
        updateCurrentDateDisplay()
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

    // Select a specific day
    fun selectDay(dayIndex: Int) {
        _selectedDay.value = dayIndex
        updateCurrentDateDisplay()
        // Here you would also load data for the selected day
        loadDataForSelectedDay()
    }

    // Move to previous week
    fun goToPreviousWeek() {
        _currentWeekOffset.value = (_currentWeekOffset.value ?: 0) - 1
        updateCurrentDateDisplay()
        loadDataForSelectedDay()
    }

    // Move to next week
    fun goToNextWeek() {
        val newOffset = (_currentWeekOffset.value ?: 0) + 1
        // Don't allow going beyond current week
        if (newOffset <= 0) {
            _currentWeekOffset.value = newOffset
            updateCurrentDateDisplay()
            loadDataForSelectedDay()
        }
    }

    // Update the display text for current date
    private fun updateCurrentDateDisplay() {
        val calendar = Calendar.getInstance()
        val weekOffset = _currentWeekOffset.value ?: 0
        val selectedDay = _selectedDay.value ?: 0

        // Adjust calendar to selected week
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset)

        // Set day of week
        val firstDayOfWeek = when (calendar.firstDayOfWeek) {
            Calendar.SUNDAY -> Calendar.MONDAY  // If week starts on Sunday, we need to adjust
            else -> calendar.firstDayOfWeek
        }

        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        calendar.add(Calendar.DAY_OF_WEEK, selectedDay)

        val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
        val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        val dayOfWeek = dayOfWeekFormat.format(calendar.time)

        // Check if it's today
        val today = Calendar.getInstance()
        val isToday = (today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR))

        _currentDateDisplay.value = if (isToday) {
            "Today: $formattedDate ($dayOfWeek)"
        } else {
            "$dayOfWeek: $formattedDate"
        }
    }

    // Load data for the currently selected day
    private fun loadDataForSelectedDay() {
        // Here you would get the date for the selected day and week offset
        // and load any existing data from your database
        val calendar = Calendar.getInstance()
        val weekOffset = _currentWeekOffset.value ?: 0
        val selectedDay = _selectedDay.value ?: 0

        // Adjust calendar to selected date
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset)
        val firstDayOfWeek = when (calendar.firstDayOfWeek) {
            Calendar.SUNDAY -> Calendar.MONDAY
            else -> calendar.firstDayOfWeek
        }
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        calendar.add(Calendar.DAY_OF_WEEK, selectedDay)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = dateFormat.format(calendar.time)

        // TODO: Load data for this date from your repository
        // Example: repository.getLogEntryForDate(dateString)?.let { logEntry ->
        //    // Update the LiveData with loaded values
        // }
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