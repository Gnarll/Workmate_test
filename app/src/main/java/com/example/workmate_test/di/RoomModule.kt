package com.example.workmate_test.di

import android.content.Context
import androidx.room.Room
import com.example.workmate_test.data.datasources.local.ApplicationDatabase
import com.example.workmate_test.data.datasources.local.dao.CountryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideApplicationDatabase(@ApplicationContext context: Context): ApplicationDatabase =
        Room.databaseBuilder(
            context,
            ApplicationDatabase::class.java,
            ApplicationDatabase.DB_NAME
        ).build()

    @Provides
    @Singleton
    fun provideCountryDao(db: ApplicationDatabase): CountryDao = db.countryDao()
}