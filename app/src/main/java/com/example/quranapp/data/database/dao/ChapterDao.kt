package com.example.quranapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.quranapp.data.model.Chapters

@Dao
interface ChapterDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAllChapters(chapters: List<Chapters>)

    @Update
    fun updateChapters(chapters: Chapters)

    @Query("SELECT * FROM chapters_Table ORDER BY id")
    fun getAllChaptersData(): List<Chapters>

    @Query("SELECT COUNT(*) FROM chapters_Table")
    fun countChapters(): Int

    @Query("SELECT * FROM chapters_Table WHERE fav_id = 0 ")
    fun updateFavId(): Int

    @Query("SELECT * FROM chapters_Table WHERE fav_id = 1")
    fun getFavId(): List<Chapters>
}