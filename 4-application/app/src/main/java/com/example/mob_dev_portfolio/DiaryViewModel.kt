package com.example.mob_dev_portfolio

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mob_dev_portfolio.data.database.AppDatabase
import com.example.mob_dev_portfolio.data.entity.DiaryEntry
import com.example.mob_dev_portfolio.data.repository.DiaryRepository
import kotlinx.coroutines.launch

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    private val diaryDao = AppDatabase.getDatabase(application).diaryDao()
    private val repository = DiaryRepository(diaryDao)

    val allEntries: LiveData<List<DiaryEntry>> = repository.getFilteredEntries("")

    fun insert(entry: DiaryEntry) = viewModelScope.launch {
        repository.insertDiaryEntry(entry)
    }

    fun update(entry: DiaryEntry) = viewModelScope.launch {
        repository.updateDiaryEntry(entry)
    }

    fun delete(id: Long) = viewModelScope.launch {
        repository.deleteDiaryEntry(id)
    }

    fun getEntriesByDate(date: String): LiveData<List<DiaryEntry>> {
        return repository.getEntriesByDate(date)
    }

    fun getAllEntriesFiltered(filter: String): LiveData<List<DiaryEntry>> {
        return repository.getFilteredEntries(filter)
    }
}