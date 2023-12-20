package com.example.quranapp.data.api

import com.example.quranapp.data.model.Quran
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface QuranApiInterface {
    @GET("api/v4/chapters")
    fun getApi(
        @Query("language") en: String
    ): Call<Quran>

    @GET("api/v4/quran/verses/indopak")
    fun getVersesapi(
        @Query("chapter_number") chapterNumber : String
    ): Call<Quran>
}