package com.example.mob_dev_portfolio;

import java.util.List;

// This data class is the main container class for the API response
data class WeatherResponse(
        val weather:List<Weather>,
        val main: Main,
        val name: String,
        val dt: Long,
        val sys: Sys,
        val wind: Wind,
        val clouds: Clouds
)
