//package com.example.mob_dev_portfolio
//
//import android.content.Context
//import android.content.SharedPreferences
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//
//class PreferenceManager(context: Context) {
//
//    private val PREFS_NAME = "EczemaAppPrefs"
//    private val KEY_GOALS = "goals"
//    private val KEY_DAYS_LOGGED = "days_logged"
//    private val KEY_LAST_FLAREUP = "last_flareup"
//    private val KEY_USER_XP = "user_xp"
//    private val KEY_LAST_WEATHER_UPDATE = "last_weather_update"
//    private val KEY_WEATHER_DATA = "weather_data"
//
//    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//    private val gson = Gson()
//
//    /**
//     * Saves list of goals to SharedPreferences
//     */
//    fun saveGoals(goals: List<GoalAdapter.Goal>) {
//        val goalsJson = gson.toJson(goals)
//        prefs.edit().putString(KEY_GOALS, goalsJson).apply()
//    }
//
//    /**
//     * Loads list of goals from SharedPreferences
//     */
//    fun loadGoals(): MutableList<GoalAdapter.Goal> {
//        val goalsJson = prefs.getString(KEY_GOALS, null)
//        return if (goalsJson != null) {
//            val type = object : TypeToken<List<GoalAdapter.Goal>>() {}.type
//            gson.fromJson(goalsJson, type)
//        } else {
//            // Return sample goals if nothing is saved yet
//            mutableListOf(
//                GoalAdapter.Goal(text = "Take morning medication", isChecked = false),
//                GoalAdapter.Goal(text = "Apply moisturizer", isChecked = false),
//                GoalAdapter.Goal(text = "Log today's symptoms", isChecked = false),
//                GoalAdapter.Goal(text = "Drink 8 glasses of water", isChecked = false)
//            )
//        }
//    }
//
//    /**
//     * Gets number of days the user has logged data
//     */
//    fun getDaysLogged(): Int {
//        return prefs.getInt(KEY_DAYS_LOGGED, 1)
//    }
//
//    /**
//     * Increments the days logged counter
//     */
//    fun incrementDaysLogged() {
//        val currentDays = getDaysLogged()
//        prefs.edit().putInt(KEY_DAYS_LOGGED, currentDays + 1).apply()
//    }
//
//    /**
//     * Gets timestamp of the last recorded flare-up
//     */
//    fun getLastFlareupDate(): Long {
//        return prefs.getLong(KEY_LAST_FLAREUP, 0)
//    }
//
//    /**
//     * Saves the timestamp of a flare-up
//     */
//    fun saveFlareup(timestamp: Long = System.currentTimeMillis()) {
//        prefs.edit().putLong(KEY_LAST_FLAREUP, timestamp).apply()
//    }
//
//    /**
//     * Gets user's current XP
//     */
//    fun getUserXP(): Int {
//        return prefs.getInt(KEY_USER_XP, 0)
//    }
//
//    /**
//     * Saves user's XP points
//     */
//    fun saveUserXP(xp: Int) {
//        prefs.edit().putInt(KEY_USER_XP, xp).apply()
//    }
//
//    /**
//     * Saves timestamp of last weather update
//     */
//    private fun saveLastWeatherUpdateTime() {
//        prefs.edit().putLong(KEY_LAST_WEATHER_UPDATE, System.currentTimeMillis()).apply()
//    }
//
//    /**
//     * Checks if weather should be updated based on the time since last update
//     */
//    fun shouldUpdateWeather(): Boolean {
//        val lastUpdate = prefs.getLong(KEY_LAST_WEATHER_UPDATE, 0)
//        return System.currentTimeMillis() - lastUpdate > ApiConstants.WEATHER_UPDATE_INTERVAL
//    }
//
//    /**
//     * Saves weather data
//     */
//    fun saveLatestWeatherData(weatherData: WeatherResponse) {
//        val weatherJson = gson.toJson(weatherData)
//        prefs.edit()
//            .putString(KEY_WEATHER_DATA, weatherJson)
//            .apply()
//
//        // Update last weather update time
//        saveLastWeatherUpdateTime()
//    }
//
//    /**
//     * Gets the latest saved weather data
//     */
//    fun getLatestWeatherData(): WeatherResponse? {
//        val weatherJson = prefs.getString(KEY_WEATHER_DATA, null)
//        return if (weatherJson != null) {
//            gson.fromJson(weatherJson, WeatherResponse::class.java)
//        } else {
//            null
//        }
//    }
//
//    /**
//     * Clears all saved preferences (for logout or reset)
//     */
//    fun clearAllPreferences() {
//        prefs.edit().clear().apply()
//    }
//}