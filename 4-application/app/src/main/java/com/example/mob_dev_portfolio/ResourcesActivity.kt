package com.example.mob_dev_portfolio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mob_dev_portfolio.databinding.ActivityResourcesBinding

class ResourcesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResourcesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Hide the action bar

        binding = ActivityResourcesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardNationalEczema.setOnClickListener {
            openWebsite("https://nationaleczema.org/")
        }

        binding.cardEczemaOrg.setOnClickListener {
            openWebsite("https://www.eczema.org/")
        }

        binding.cardAad.setOnClickListener {
            openWebsite("https://www.aad.org/public/diseases/eczema")
        }

        binding.cardNhs.setOnClickListener {
            openWebsite("https://www.nhs.uk/conditions/atopic-eczema/")
        }

        binding.cardMayo.setOnClickListener {
            openWebsite("https://www.mayoclinic.org/diseases-conditions/atopic-dermatitis-eczema/")
        }

        // Back button click listener
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    // Helper function to open websites
    private fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}