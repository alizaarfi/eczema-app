package com.example.mob_dev_portfolio

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mob_dev_portfolio.GoalAdapter
import com.example.mob_dev_portfolio.databinding.ActivityHomeBinding
import com.example.mob_dev_portfolio.databinding.ActivityMainBinding
import android.content.Intent

class HomeActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeBinding
    private lateinit var goalAdapter: GoalAdapter
    private val goalsList = mutableListOf<GoalAdapter.Goal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Setup RecyclerView
        goalAdapter = GoalAdapter(goalsList)
        binding.recyclerItems.layoutManager = LinearLayoutManager(this)
        binding.recyclerItems.adapter = goalAdapter

        // Button click to add goal
        binding.btnAddGoal.setOnClickListener {
            val goalText = binding.etGoalInput.text.toString().trim()
            if (goalText.isNotEmpty()) {
                addGoal(goalText)
                binding.etGoalInput.text.clear()
            }


        }
        setupBottomNavigation()

    }


    // Function to add new goal
    fun addGoal(goal: String) {
        goalsList.add(GoalAdapter.Goal(text = goal))
        goalAdapter.notifyItemInserted(goalsList.size - 1)
        binding.recyclerItems.smoothScrollToPosition(goalsList.size - 1)
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Already on home page
                    true
                }

                R.id.navigation_track -> {
                    // Navigate to Track activity
//                    val intent = Intent(this, TrackActivity::class.java)
//                    startActivity(intent)
                    true
                }

                R.id.navigation_profile -> {
                    // Navigate to Profile activity
//                    val intent = Intent(this, ProfileActivity::class.java)
//                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        // Set the Home item as selected
        binding.bottomNavigation.selectedItemId = R.id.navigation_home
    }
}
