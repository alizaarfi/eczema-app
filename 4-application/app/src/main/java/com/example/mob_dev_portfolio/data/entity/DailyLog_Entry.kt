package com.example.mob_dev_portfolio.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Each entity represents a table
// The table daily_logs stores info from the daily symptom logs
@Entity(tableName = "daily_logs")
data class DailyLog_Entry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: String,
    val date: String,
    val physicalSensations: String,
    val mood: String,
    val bodyAreas: String,
    val bodyAreaSeverity: String,
    val treatments: String,
    val environments: String,
    val dietHydration: String,
    val exerciseSleep: String
)