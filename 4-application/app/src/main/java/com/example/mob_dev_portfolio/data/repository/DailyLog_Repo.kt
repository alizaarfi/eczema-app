package com.example.mob_dev_portfolio.data.repository

import com.example.mob_dev_portfolio.data.entity.DailyLog_Entry
import com.example.mob_dev_portfolio.data.dao.DailyLog_Dao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// This a repo for
// All the suspend funcs can only be called from a coroutine
class DailyLog_Repo(private val dailyLogDao: DailyLog_Dao) {

    // checks for existing logs
    suspend fun insertOrUpdateLog(logEntry: DailyLog_Entry) {
        val existingLog = getLogByDate(logEntry.date)
        if (existingLog != null) {
            // Create a copy with the existing ID
            val updatedLog = logEntry.copy(id = existingLog.id)
            updateLog(updatedLog)
        } else {
            insertLog(logEntry)
        }
    }

    suspend fun insertLog(logEntry: DailyLog_Entry): Long {
        return dailyLogDao.insertLog(logEntry)
    }

    suspend fun updateLog(logEntry: DailyLog_Entry) {
        dailyLogDao.updateLog(logEntry)
    }

    suspend fun getLogByDate(date: String): DailyLog_Entry? {
        return dailyLogDao.getLogByDate(date)
    }

    suspend fun getAllLogs(): List<DailyLog_Entry> {
        return dailyLogDao.getAllLogs()
    }

    suspend fun deleteLog(id: Long) {
        dailyLogDao.deleteLog(id)
    }
    // converting methods for body areas
    fun convertBodyAreasToString(bodyAreas: Map<String, Int>): Pair<String, String> {
        val areas = bodyAreas.keys.joinToString(",")
        val severities = Gson().toJson(bodyAreas)
        return Pair(areas, severities)
    }

    fun convertStringToBodyAreas(areas: String, severities: String): Map<String, Int> {
        if (areas.isBlank() || severities.isBlank()) return emptyMap()

        val type = object : TypeToken<Map<String, Int>>() {}.type
        return try {
            Gson().fromJson(severities, type)
        } catch (e: Exception) {
            // Fallback parsing if JSON fails
            val areasList = areas.split(",")
            val defaultSeverity = 1
            areasList.associateWith { defaultSeverity }
        }
    }
}