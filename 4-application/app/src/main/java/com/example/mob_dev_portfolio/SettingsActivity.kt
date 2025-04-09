package com.example.mob_dev_portfolio

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.mob_dev_portfolio.data.PrefRepo


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private lateinit var viewModel: SettingsViewModel

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            // Initialize the ViewModel
            val prefRepo = PrefRepo(requireContext().dataStore)
            viewModel = ViewModelProvider(this, SettingsViewModelFactory(prefRepo))
                .get(SettingsViewModel::class.java)

            // Get references to preference items
            val locationPreference = findPreference<ListPreference>("simulated_location")
            val conditionPreference = findPreference<ListPreference>("simulated_condition")

            // Setup location preference change listener
            locationPreference?.setOnPreferenceChangeListener { _, newValue ->
                val selectedLocation = newValue as String
                Log.d("SettingsFragment", "Location changed to: $selectedLocation")
                viewModel.saveLocation(selectedLocation)
                true
            }

            // Setup condition preference change listener
            conditionPreference?.setOnPreferenceChangeListener { _, newValue ->
                val selectedCondition = newValue as String
                Log.d("SettingsFragment", "Weather condition changed to: $selectedCondition")
                viewModel.saveWeatherCondition(selectedCondition)
                true
            }

            // Observe the ViewModel's LiveData to update the UI when needed
            viewModel.selectedLocation.observe(this) { location ->
                locationPreference?.summary = "Current location: $location"
            }

            viewModel.simulatedCondition.observe(this) { condition ->
                conditionPreference?.summary = "Current condition: $condition"
            }
        }
    }
}