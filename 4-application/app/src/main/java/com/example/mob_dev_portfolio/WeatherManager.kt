package com.example.mob_dev_portfolio

import android.content.Context
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherManager(private val context: Context) {

    private val TAG = "WeatherManager"
    private val weatherService: WeatherService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherService = retrofit.create(WeatherService::class.java)
    }

   // Fetches based on the location coordinates
    fun fetchWeatherData(
        latitude: Double,
        longitude: Double,
        apiKey: String = ApiConstants.WEATHER_API_KEY,
        onSuccess: (WeatherResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d(TAG, "Fetching weather data for latitude: $latitude, longitude: $longitude")

        weatherService.getCurrentWeather(latitude, longitude, apiKey, ApiConstants.UNITS_METRIC)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                    if (response.isSuccessful) {
                        val weatherData = response.body()
                        if (weatherData != null) {
                            Log.d(TAG, "Weather data received: ${weatherData.weather[0].main}")
                            onSuccess(weatherData)
                        } else {
                            Log.e(TAG, "Empty response body")
                            onError(ApiConstants.ERROR_EMPTY_RESPONSE)
                        }
                    } else {
                        Log.e(TAG, "API error: ${response.code()} - ${response.message()}")
                        onError("${ApiConstants.ERROR_API} (${response.code()})")
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.e(TAG, "Network error: ${t.message}", t)
                    onError(ApiConstants.ERROR_NETWORK)
                }
            })
    }


    // This returns the appropriate weather icon(e.g snow) based on the weather condition code
    // Using code as standardised by OpenWeatherAPI instead of easier with strings
    fun getWeatherIconResource(weatherId: Int): Int {
        return when (weatherId) {
            in 200..232 -> R.drawable.thunderstorm_icon
            in 300..321 -> R.drawable.drizzle_icon
            in 500..531 -> R.drawable.rainy_icon
            in 600..622 -> R.drawable.snow_icon
            in 701..781 -> R.drawable.foggy_icon
            800 -> R.drawable.sunny_icon
            in 801..804 -> R.drawable.cloudy_icon
            else -> R.drawable.sunny_icon
        }
    }



    // Analysis for potential eczema triggers
    fun analyseEczemaRisk(weatherData: WeatherResponse): EczemaRiskFactor {
        // Low humidity can be a trigger (below 45%)
        val isLowHumidity = weatherData.main.humidity < 45

        // High temperatures can be a trigger (above 25°C)
        val isHighTemperature = weatherData.main.temp > 25.0

        // Low temperatures can be a trigger (below 5°C)
        val isLowTemperature = weatherData.main.temp < 5.0

        // Strong winds can cause dryness (above 8 m/s)
        val isWindy = weatherData.wind?.speed ?: 0.0 > 8.0

        // High UV (typically on clear, sunny days)
        val isPotentialHighUV = weatherData.weather[0].id == 800 &&
                isTimeOfDayForHighUV() &&
                !isWinter()

        // Count risk factors
        val riskFactors = listOf(
            isLowHumidity,
            isHighTemperature,
            isLowTemperature,
            isWindy,
            isPotentialHighUV
        ).count { it }

        return when {
            riskFactors >= 3 -> EczemaRiskFactor.HIGH
            riskFactors >= 1 -> EczemaRiskFactor.MODERATE
            else -> EczemaRiskFactor.LOW
        }
    }


    // Gets a description of the conditions
    fun getWeatherDescription(weatherData: WeatherResponse): String {
        val condition = weatherData.weather[0].description.capitalize(Locale.getDefault())
        val temp = "${weatherData.main.temp.toInt()}°C"

        return "$condition, $temp"
    }

    // Gets related tips to show based on conditions to relate to eczema
    fun getEczemaTips(weatherData: WeatherResponse): List<String> {
        val tips = mutableListOf<String>()

        // Low humidity tips
        if (weatherData.main.humidity < 45) {
            tips.add("Low humidity detected. Consider using a humidifier. \nApply moisturiser more frequently today!")
            tips.add("Apply moisturiser more frequently today.")
        }

        // Hot weather tips
        if (weatherData.main.temp > 25.0) {
            tips.add("Hot weather detected. Stay cool and avoid sweating which can trigger flares.\nPollen is also high - if you have hayfever remember to take antihistamines. \n Wear loose, cotton clothing! ")
            tips.add("Wear loose, cotton clothing.")
        }

        // Cold weather tips
        if (weatherData.main.temp < 5.0) {
            tips.add("Cold weather detected. \nProtect your skin from cold with layers. \nUse thicker moisturisers in cold weather!")
            tips.add("Use thicker moisturisers in cold weather.")
        }

        // Windy conditions
        if (weatherData.wind?.speed ?: 0.0 > 8.0) {
            tips.add("Windy conditions detected. Windy conditions may dry skin. \nProtect exposed areas!")
        }

        // Sunny day
        if (weatherData.weather[0].id == 800 && isTimeOfDayForHighUV() && !isWinter()) {
            tips.add("Sunny day! Remember wear sunscreen and protective clothing.")
        }

        // If no specific tips, provide general advice
        if (tips.isEmpty()) {
            tips.add("Remember to stay hydrated and moisturise regularly.")
        }

        return tips
    }


    // Chcecks if it is it the time of day with high UV?
    private fun isTimeOfDayForHighUV(): Boolean {
        val currentHour = SimpleDateFormat("HH", Locale.getDefault()).format(Date()).toInt()
        return currentHour in 10..16
    }

    // Checks if it is winter
    private fun isWinter(): Boolean {
        val currentMonth = SimpleDateFormat("MM", Locale.getDefault()).format(Date()).toInt()
        return currentMonth in listOf(12, 1, 2)
    }
}
