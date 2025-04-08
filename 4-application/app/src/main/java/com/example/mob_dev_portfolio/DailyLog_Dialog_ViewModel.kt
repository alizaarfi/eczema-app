package com.example.mob_dev_portfolio

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mob_dev_portfolio.data.database.AppDatabase
import com.example.mob_dev_portfolio.data.entity.DailyLog_Entry
import com.example.mob_dev_portfolio.data.repository.DailyLog_Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class DailyLog_Dialog_ViewModel(application: Application) : AndroidViewModel(application) {
    // Repo for the room db
    private val repo: DailyLog_Repo

    // All LiveData stuff
    // Selected day where 0 = Monday and 6 = Sunday
    private val _selectedDay = MutableLiveData<Int>()
    val selectedDay: LiveData<Int> = _selectedDay

    // Current week offset where 0 = current week and -1 = last week
    private val _currentWeekOffset = MutableLiveData<Int>(0)
    val currentWeekOffset: LiveData<Int> = _currentWeekOffset

    // Todays day
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

    init {
        // Initialise current day
        val calendar = Calendar.getInstance()
        // Convert to our day index (0 = Monday, 6 = Sunday)
        val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> 6
            else -> calendar.get(Calendar.DAY_OF_WEEK) - 2
        }
        _todayDayOfWeek.value = dayOfWeek
        _selectedDay.value = dayOfWeek
        updateCurrentDateDisplay()

        // Initialise the repo
        val dailyLogDao = AppDatabase.getDatabase(application).dailyLogDao()
        repo = DailyLog_Repo(dailyLogDao)
    }

    // Format date
    private fun getFormattedDate(): String {
        val calendar = Calendar.getInstance()
        val weekOffset = _currentWeekOffset.value ?: 0
        val selectedDay = _selectedDay.value ?: 0
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset)
        val firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        calendar.add(Calendar.DAY_OF_WEEK, selectedDay)

        // Format date as yyyy-MM-dd for room db storage
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(calendar.time)
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
        loadLogForSelectedDate()
    }

    // Move to previous week
    fun goToPreviousWeek() {
        _currentWeekOffset.value = (_currentWeekOffset.value ?: 0) - 1
        updateCurrentDateDisplay()
        loadLogForSelectedDate()
    }

    // Move to next week
    fun goToNextWeek() {
        val newOffset = (_currentWeekOffset.value ?: 0) + 1
        // Can't go past the current week
        if (newOffset <= 0) {
            _currentWeekOffset.value = newOffset
            updateCurrentDateDisplay()
            loadLogForSelectedDate()
        }
    }
    // Update the display text for current date
    private fun updateCurrentDateDisplay() {
        val calendar = Calendar.getInstance()
        val weekOffset = _currentWeekOffset.value ?: 0
        val selectedDay = _selectedDay.value ?: 0

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

     fun loadLogForSelectedDate(date: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            // Get the date string for database lookup
            val dateString = getFormattedDate()

            // Load log entry from database
            val logEntry = repo.getLogByDate(dateString)

            // Update LiveData values on main thread
            withContext(Dispatchers.Main) {
                if (logEntry != null) {
                    // Update each selection
                    _selectedPhysicalSensations.value =
                        if (logEntry.physicalSensations.isEmpty()) setOf()
                        else logEntry.physicalSensations.split(",").toSet()

                    _selectedMood.value = logEntry.mood

                    _selectedBodyAreas.value =
                        repo.convertStringToBodyAreas(logEntry.bodyAreas, logEntry.bodyAreaSeverity)

                    _selectedTreatments.value =
                        if (logEntry.treatments.isEmpty()) setOf()
                        else logEntry.treatments.split(",").toSet()

                    _selectedEnvironments.value =
                        if (logEntry.environments.isEmpty()) setOf()
                        else logEntry.environments.split(",").toSet()

                    _selectedDietHydration.value =
                        if (logEntry.dietHydration.isEmpty()) setOf()
                        else logEntry.dietHydration.split(",").toSet()

                    _selectedExerciseSleep.value =
                        if (logEntry.exerciseSleep.isEmpty()) setOf()
                        else logEntry.exerciseSleep.split(",").toSet()

                } else {
                    // May be why when updating a past log it resets
                    _selectedPhysicalSensations.value = setOf()
                    _selectedMood.value = ""
                    _selectedBodyAreas.value = mapOf()
                    _selectedTreatments.value = setOf()
                    _selectedEnvironments.value = setOf()
                    _selectedDietHydration.value = setOf()
                    _selectedExerciseSleep.value = setOf()
                }
            }
        }
    }

    // Called by fragment and calls the repo for roomdb
    fun saveLog(
        physicalSensations: String,
        mood: String,
        treatments: String,
        environments: String,
        dietHydration: String,
        exerciseSleep: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            // Calls repo func to convert selected body areas to strings
            val (bodyAreasStr, bodyAreaSeverityStr) = repo.convertBodyAreasToString(selectedBodyAreas.value ?: emptyMap())

            // Create log entry
            val logEntry = DailyLog_Entry(
                timestamp = System.currentTimeMillis().toString(),
                date = currentDateDisplay.value ?: getFormattedDate(),
                physicalSensations = physicalSensations,
                mood = mood,
                bodyAreas = bodyAreasStr,
                bodyAreaSeverity = bodyAreaSeverityStr,
                treatments = treatments,
                environments = environments,
                dietHydration = dietHydration,
                exerciseSleep = exerciseSleep
            )

            // Saves to the roomdb!
            repo.insertOrUpdateLog(logEntry)
        }
    }

}