package com.example.mob_dev_portfolio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mob_dev_portfolio.databinding.ActivityFirstAidBinding

class FirstAidActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstAidBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Hide the action bar

        binding = ActivityFirstAidBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up section expanders
        binding.cardMildSymptoms.setOnClickListener {
            toggleVisibility(binding.expandableMild)
        }

        binding.cardModerateSymptoms.setOnClickListener {
            toggleVisibility(binding.expandableModerate)
        }

        binding.cardSevereSymptoms.setOnClickListener {
            toggleVisibility(binding.expandableSevere)
        }

        // Set up emergency service links
        binding.cardFindDermatologist.setOnClickListener {
            openWebsite("https://www.nhs.uk/service-search/other-health-services/dermatology")
        }


        // Back button click listener
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Initialize with all sections collapsed except mild
        binding.expandableMild.visibility = android.view.View.VISIBLE
        binding.expandableModerate.visibility = android.view.View.GONE
        binding.expandableSevere.visibility = android.view.View.GONE
    }

    private fun toggleVisibility(view: android.view.View) {
        if (view.visibility == android.view.View.VISIBLE) {
            view.visibility = android.view.View.GONE
        } else {
            view.visibility = android.view.View.VISIBLE
        }
    }

    private fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}