package com.example.mob_dev_portfolio

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mob_dev_portfolio.databinding.ActivityHomeBinding
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.mob_dev_portfolio.data.PrefRepo
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var notificationUtils: NotificationUtils
    private val goalsViewModel: GoalsViewModel by viewModels()
    private lateinit var goalAdapter: GoalAdapter
    private lateinit var settingsViewModel: SettingsViewModel

    private lateinit var weatherManager: WeatherManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialise before use
        notificationUtils = NotificationUtils()
        settingsViewModel = SettingsViewModel(PrefRepo(dataStore))

        // Observe lat/lon and only fetch weather once they're valid
        settingsViewModel.latitude.observe(this) { lat ->
            val lon = settingsViewModel.longitude.value
            if (!lat.isNullOrBlank() && !lon.isNullOrBlank()) {
                fetchAndDisplayWeather()
            }
        }

        // Delays fetching the api data before get long/lat from datastore
        settingsViewModel.longitude.observe(this) { lon ->
            val lat = settingsViewModel.latitude.value
            if (!lat.isNullOrBlank() && !lon.isNullOrBlank()) {
                fetchAndDisplayWeather()
            }
        }

        // Observe selected location and update UI
        settingsViewModel.selectedLocation.observe(this) { location ->
            binding.tvLocation.text = location
        }

        // Requests permission to user if not granted yet
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
        NotificationHelper.createNotificationChannels(this)

        val navView: NavigationView = binding.navView
        val progressGoals: ProgressBar = binding.progressGoals

        // Setup toolbar with proper theme
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Home"

        // Setup drawer toggle with proper theming
        toggle = ActionBarDrawerToggle(
            this,
            binding.main,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.main.addDrawerListener(toggle)
        toggle.syncState()

//        setSupportActionBar(binding.toolbar)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

//        toggle = ActionBarDrawerToggle(
//            this,
//            binding.main,
//            binding.toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    binding.main.closeDrawers() // Close drawer after selection
                    true
                }
                R.id.navigation_resources -> {
                    val intent = Intent(this, ResourcesActivity::class.java)
                    startActivity(intent)
                    binding.main.closeDrawers() // Close drawer after selection
                    true
                }
                R.id.navigation_SOS -> {
                    val intent = Intent(this, FirstAidActivity::class.java)
                    startActivity(intent)
                    binding.main.closeDrawers() // Close drawer after selection
                    true
                }
                else -> false
            }
        }

//        navView.setNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.navigation_settings -> {
//                    // Handle Settings item click
//                    val intent = Intent(this, SettingsActivity::class.java)
//                    startActivity(intent)
//                    true
//                }
//                R.id.navigation_resources -> {
//                    val intent = Intent(this, ResourcesActivity::class.java)
//                    startActivity(intent)
//                    true
//                }
//                R.id.navigation_SOS -> {
//                    val intent = Intent(this, FirstAidActivity::class.java)
//                    startActivity(intent)
//                    true
//                }
//                else -> false
//            }
//        }

        // Setup RecyclerView with GoalAdapter
        goalAdapter = GoalAdapter { updatedGoal ->
            goalsViewModel.updateGoalCompletion(updatedGoal)
        }
        binding.recyclerItems.adapter = goalAdapter
        binding.recyclerItems.layoutManager = LinearLayoutManager(this)

        // Observes the livedata for updating UI
        goalsViewModel.goals.observe(this) { goalList ->
            goalAdapter.submitList(goalList)
            updateGoalProgress(goalList)
        }

        binding.btnAddGoal.setOnClickListener {
            val text = binding.etGoalInput.text.toString().trim()
            if (text.isNotEmpty()) {
                notificationUtils.showNotification(
                    this,
                    "medication_reminders",
                    "Medication Goal Reminder",
                    "You added a goal: $text",
                    101
                )

                goalsViewModel.addGoal(text)
                binding.etGoalInput.text.clear()
            }
        }

        weatherManager = WeatherManager(this)
        setupBottomNavigation()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar, menu)
        return true
    }

    // Directly call and update the card
    fun fetchAndDisplayWeather() {
        val latitude = settingsViewModel.latitude.value?.toDoubleOrNull() ?: 51.481583
        val longitude = settingsViewModel.longitude.value?.toDoubleOrNull() ?: -3.179090

        weatherManager.fetchWeatherData(
            latitude =  latitude,
            longitude = longitude,
            apiKey = ApiConstants.WEATHER_API_KEY,
            onSuccess = { weatherData ->
                val risk = weatherManager.analyseEczemaRisk(weatherData)
                val tips = weatherManager.getEczemaTips(weatherData)

                // Update risk level
                binding.tvRiskLevel.text = risk.toString()
                when (risk) {
                    EczemaRiskFactor.LOW -> binding.tvRiskLevel.setTextColor(
                        ContextCompat.getColor(this, android.R.color.holo_green_dark))
                    EczemaRiskFactor.MODERATE -> binding.tvRiskLevel.setTextColor(
                        ContextCompat.getColor(this, android.R.color.holo_orange_dark))
                    else -> binding.tvRiskLevel.setTextColor(
                        ContextCompat.getColor(this, android.R.color.holo_red_dark))
                }

                // Update the UI
                binding.tvEczemaTips.text = tips.firstOrNull() ?: "No specific advice today."
                binding.tvWeatherTemp.text = "${weatherData.main.temp.toInt()}°C"
                binding.tvWeatherCondition.text = weatherManager.getWeatherDescription(weatherData)
                binding.ivWeatherIcon.setImageResource(
                    weatherManager.getWeatherIconResource(weatherData.weather[0].id)
                )

                // Send a notification ONLY if the risk is high
                if (risk != EczemaRiskFactor.LOW) {
                    notificationUtils.showNotification(
                        this,
                        "weather_alerts",
                        "⚠️ Eczema Trigger Alert",
                        "Environment might trigger flare-ups: $risk risk.",
                        200
                    )
                }

            },
            onError = { errorMessage ->
                // If there was an error, display the error message
                Log.e("WeatherError", errorMessage)
                binding.tvOverviewDetails.text = "Error fetching weather data."
            }
        )
    }

    private fun updateGoalProgress(goals: List<Goal>) {
        val total = goals.size
        val completed = goals.count { it.isChecked }
        val ratio = if (total > 0) (completed * 100) / total else 0

        binding.progressGoals.progress = ratio
        binding.tvGoalProgress.text = "$completed/$total Completed"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Already on home page
                    true
                }
                R.id.navigation_log -> {
                    DailyLog_Dialog_Fragment.newInstance().show(
                        supportFragmentManager,
                        DailyLog_Dialog_Fragment.TAG
                    )
                    true
                }
                R.id.navigation_diary -> {
                     val intent = Intent(this, DiaryActivity::class.java)
                     startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Set the Home item as selected
        binding.bottomNavigation.selectedItemId = R.id.navigation_home
    }

    // For settings
    override fun onResume() {
        super.onResume()
        settingsViewModel.selectedLocation.value?.let { location ->
            binding.tvLocation.text = location
        }

        val lat = settingsViewModel.latitude.value
        val lon = settingsViewModel.longitude.value
        if (!lat.isNullOrBlank() && !lon.isNullOrBlank()) {
            fetchAndDisplayWeather()
        }
    }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }


}