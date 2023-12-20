package com.example.quranapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.quranapp.data.model.Verses

@Dao
interface VersesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVerse(verses: List<Verses>)

    @Update
    fun updateVerses(verses: Verses)

    @Query("SELECT * FROM verses_Table WHERE surahIndex = :chapId")
    fun getAllVersesData(chapId: String): List<Verses>

    @Query("SELECT COUNT(*) FROM verses_Table")
    fun countVerses(): Int
}