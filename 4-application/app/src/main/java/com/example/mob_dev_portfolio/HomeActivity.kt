package com.example.mob_dev_portfolio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mob_dev_portfolio.databinding.ActivityHomeBinding
import java.util.Locale
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import java.util.UUID

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle


    private lateinit var goalAdapter: GoalAdapter
    private val goalsList = mutableListOf<GoalAdapter.Goal>() // Using your existing Goal class

    private lateinit var weatherManager: WeatherManager
    private lateinit var preferenceManager: PreferenceManager

    // Default location coordinates (can be updated with the user's location)
    private val defaultLatitude = 51.5072  // London
    private val defaultLongitude = 0.1276  // London

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

        // Setup RecyclerView with your existing adapter
        goalAdapter = GoalAdapter(goalsList)
        binding.recyclerItems.layoutManager = LinearLayoutManager(this)
        binding.recyclerItems.adapter = goalAdapter

        // Button click to add goal - using your existing method
        binding.btnAddGoal.setOnClickListener {
            val goalText = binding.etGoalInput.text.toString().trim()
            if (goalText.isNotEmpty()) {
                addGoal(goalText)
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


    // Your existing method for adding goals
    fun addGoal(goal: String) {
        goalsList.add(GoalAdapter.Goal(text = goal))
        goalAdapter.notifyItemInserted(goalsList.size - 1)
        binding.recyclerItems.smoothScrollToPosition(goalsList.size - 1)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
//            R.id.menu_settings -> {
//                // do something
//                true
//            }
//            R.id.menu_about -> {
//                // do something else
//                true
//            }
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

}