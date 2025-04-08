package com.example.mob_dev_portfolio.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mob_dev_portfolio.data.entity.DailyLog_Entry
import com.example.mob_dev_portfolio.data.dao.DailyLog_Dao

@Database(entities = [DailyLog_Entry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dailyLogDao(): DailyLog_Dao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "eczema_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}