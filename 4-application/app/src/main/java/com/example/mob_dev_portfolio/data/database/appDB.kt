package com.example.mob_dev_portfolio.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mob_dev_portfolio.data.entity.DailyLog_Entry
import com.example.mob_dev_portfolio.data.dao.DailyLog_Dao
import com.example.mob_dev_portfolio.data.dao.DiaryDao
import com.example.mob_dev_portfolio.data.entity.DiaryEntry
@Database(entities = [DailyLog_Entry::class, DiaryEntry::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dailyLogDao(): DailyLog_Dao
    abstract fun diaryDao(): DiaryDao  // Make sure this is added

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "eczema_app_database"
                )
                    .fallbackToDestructiveMigration()  // Add this line to handle migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}