package com.example.quranapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "verses_Table")
data class Verses(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val verse_key: String,
    val text_indopak: String,
    var surahIndex: String
): Parcelable
