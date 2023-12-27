package com.example.quranapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.quranapp.data.database.QuranRoomDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DBModule {
    @Provides
    @Singleton
    fun provideQuranDb(@ApplicationContext appCotext: Context): QuranRoomDb {
        return Room.databaseBuilder(appCotext, QuranRoomDb::class.java, QuranRoomDb.dbName)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

}