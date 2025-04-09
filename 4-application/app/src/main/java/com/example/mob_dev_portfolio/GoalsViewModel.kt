package com.example.mob_dev_portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class GoalsViewModel : ViewModel() {
    private val _goals = MutableLiveData<List<Goal>>(emptyList())
    val goals: LiveData<List<Goal>> get() = _goals

    fun addGoal(text: String) {
        val currentList = _goals.value?.toMutableList() ?: mutableListOf()
        currentList.add(Goal(text = text))
        _goals.value = currentList
    }

    fun updateGoalCompletion(updatedGoal: Goal) {
        val currentList = _goals.value?.toMutableList() ?: mutableListOf()
        val updatedList = currentList.map { existingGoal ->
            if (existingGoal.id == updatedGoal.id) updatedGoal else existingGoal
        }

        _goals.value = updatedList
    }
}
// ViewModel: https://developer.android.com/topic/libraries/architecture/viewmodel