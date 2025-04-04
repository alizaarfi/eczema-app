package com.example.mob_dev_portfolio;


// This data class contains the temperature and humdity data
data class MainWeather(
        val temp: Double,
        val feels_like: Double,
        val temp_min: Double,
        val temp_max: Double,
        val pressure: Int,
        val humidity: Int
)
