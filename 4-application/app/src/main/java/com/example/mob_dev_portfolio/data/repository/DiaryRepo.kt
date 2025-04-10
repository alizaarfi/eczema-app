package com.example.mob_dev_portfolio.data.repository

import androidx.lifecycle.LiveData
import com.example.mob_dev_portfolio.data.dao.DiaryDao
import com.example.mob_dev_portfolio.data.entity.DiaryEntry

class DiaryRepository(private val diaryDao: DiaryDao) {

    suspend fun insertDiaryEntry(entry: DiaryEntry): Long {
        return diaryDao.insert(entry)
    }
    suspend fun updateDiaryEntry(entry: DiaryEntry) {
        diaryDao.update(entry)
    }
    suspend fun deleteDiaryEntry(id: Long) {
        diaryDao.delete(id)
    }
    fun getEntriesByDate(date: String): LiveData<List<DiaryEntry>> {
        return diaryDao.getEntriesByDate(date)
    }

    fun getFilteredEntries(filter: String): LiveData<List<DiaryEntry>> {
        return diaryDao.getAllEntriesFiltered(filter)
    }
}
