package com.example.quranapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "chapters_Table")
data class Chapters(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val revelation_place: String,
    val revelation_order: Int,
    val bismillah_pre: String,
    val name_simple: String,
    val name_complex: String,
    val name_arabic: String,
    val verses_count: String,
    var fav_id: Int = 0
): Parcelable
