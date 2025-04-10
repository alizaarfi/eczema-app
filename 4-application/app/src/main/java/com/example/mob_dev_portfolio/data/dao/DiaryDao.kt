package com.example.mob_dev_portfolio.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mob_dev_portfolio.data.entity.DiaryEntry

@Dao
interface DiaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DiaryEntry): Long
    @Update
    suspend fun update(entry: DiaryEntry)
    @Query("DELETE FROM diary_entries WHERE id = :id")
    suspend fun delete(id: Long)
    @Query("SELECT * FROM diary_entries ORDER BY date DESC")
    fun getAllEntries(): LiveData<List<DiaryEntry>>
    @Query("SELECT * FROM diary_entries WHERE date = :date")
    fun getEntriesByDate(date: String): LiveData<List<DiaryEntry>>

    @Query("SELECT * FROM diary_entries WHERE foodName LIKE '%' || :filter || '%' OR skincareName LIKE '%' || :filter || '%' OR reactionSeverity LIKE '%' || :filter || '%' ORDER BY date DESC")
    fun getAllEntriesFiltered(filter: String): LiveData<List<DiaryEntry>>
}

