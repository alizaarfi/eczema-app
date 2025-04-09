package com.example.mob_dev_portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mob_dev_portfolio.data.PrefRepo

// For settings viewmodel
class SettingsViewModelFactory(private val prefRepo: PrefRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(prefRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}