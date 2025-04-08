package com.example.mob_dev_portfolio.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mob_dev_portfolio.data.entity.DailyLog_Entry

// This defines data access objects so that i can interact with the stored data
@Dao
interface DailyLog_Dao {
    @Insert
    suspend fun insertLog(logEntry: DailyLog_Entry): Long

    @Update
    suspend fun updateLog(logEntry: DailyLog_Entry)

    @Query("SELECT * FROM daily_logs WHERE date = :date")
    suspend fun getLogByDate(date: String): DailyLog_Entry?

    @Query("SELECT * FROM daily_logs ORDER BY date DESC")
    suspend fun getAllLogs(): List<DailyLog_Entry>

    @Query("DELETE FROM daily_logs WHERE id = :id")
    suspend fun deleteLog(id: Long)
}