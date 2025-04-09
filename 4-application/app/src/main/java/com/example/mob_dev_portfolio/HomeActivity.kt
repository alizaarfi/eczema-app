package com.example.mob_dev_portfolio

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private val goalsViewModel: GoalsViewModel by viewModels()
    private lateinit var goalAdapter: GoalAdapter

    private lateinit var weatherManager: WeatherManager
//    private lateinit var preferenceManager: PreferenceManager

    // Default location coordinates (can be updated with the user's location)
    private val defaultLatitude = 51.5072  // London
    private val defaultLongitude = 0.1276  // London

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Requests permission to user if not granted yet
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

        val navView: NavigationView = binding.navView
        val progressGoals: ProgressBar = binding.progressGoals

        setSupportActionBar(binding.toolbar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toggle = ActionBarDrawerToggle(
            this,
            binding.main, // your DrawerLayout ID
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.main.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_settings -> {
                    // Handle Settings item click
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Setup RecyclerView with GoaAdapter
        goalAdapter = GoalAdapter { updatedGoal ->
            goalsViewModel.updateGoalCompletion(updatedGoal)
        }
        binding.recyclerItems.adapter = goalAdapter
        binding.recyclerItems.layoutManager = LinearLayoutManager(this)

        // Observes the livedata (e.g when a goal is checked) to update the UI
        goalsViewModel.goals.observe(this) { goalList ->
            goalAdapter.submitList(goalList)
            updateGoalProgress(goalList)
        }

        // Button click to add goal - using your existing method
        binding.btnAddGoal.setOnClickListener {
            val text = binding.etGoalInput.text.toString().trim()
            if (text.isNotEmpty()) {
                goalsViewModel.addGoal(text)
                binding.etGoalInput.text.clear()
            }
        }



//        // Initialize managers
//        weatherManager = WeatherManager(this)
//        preferenceManager = PreferenceManager(this)
//        updateOverviewCard()
//        fetchWeatherData()
        setupBottomNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar, menu)
        return true
    }


//    fun addGoal(goal: String) {
//        goalsList.add(GoalAdapter.Goal(text = goal))
//        goalAdapter.notifyItemInserted(goalsList.size - 1)
//        binding.recyclerItems.smoothScrollToPosition(goalsList.size - 1)
//        updateGoalProgress()
//    }

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
//                R.id.navigation_diary -> {
//                     val intent = Intent(this, DiaryActivity::class.java)
//                     startActivity(intent)
//                    true
//                }
                else -> false
            }
        }

        // Set the Home item as selected
        binding.bottomNavigation.selectedItemId = R.id.navigation_home
    }

}