package com.example.mob_dev_portfolio

object ApiConstants {
    // Base URL for OpenWeatherMap API
    const val WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"

    // My API key
    // For deployment should store in gradle properties file
    const val WEATHER_API_KEY = "7ed851f01b84744ab9697891ca9c964b"

    // Units
    const val UNITS_METRIC = "metric"
    const val UNITS_IMPERIAL = "imperial"

    // Update interval in milliseconds (1 hour)
    const val WEATHER_UPDATE_INTERVAL = 3600000L

    // Error messages
    const val ERROR_NETWORK = "Network error occurred. Please check your connection."
    const val ERROR_API = "Error fetching weather data. Please try again later."
    const val ERROR_EMPTY_RESPONSE = "No weather data available."
}