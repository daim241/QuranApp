package com.example.quranapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quranapp.data.database.dao.ChapterDao
import com.example.quranapp.data.database.dao.VersesDao
import com.example.quranapp.data.model.Chapters
import com.example.quranapp.data.model.Verses

@Database(entities = [
    Chapters::class,
Verses::class
                     ], version = 1, exportSchema = false)
abstract class QuranRoomDb: RoomDatabase() {

    abstract fun chaptersDao(): ChapterDao
    abstract fun versesDao(): VersesDao

    companion object {
        private var INSTANCE: QuranRoomDb? = null
        fun getQuranDB(context: Context): QuranRoomDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuranRoomDb::class.java,
                    "quran_app"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}