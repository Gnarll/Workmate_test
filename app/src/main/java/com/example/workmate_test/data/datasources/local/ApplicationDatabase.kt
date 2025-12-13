package com.example.workmate_test.data.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.workmate_test.data.datasources.local.converters.CountryConverter
import com.example.workmate_test.data.datasources.local.dao.CountryDao
import com.example.workmate_test.data.datasources.local.entities.CountryEntity

@Database(entities = [CountryEntity::class], exportSchema = true, version = 1)
@TypeConverters(CountryConverter::class)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao

    companion object {
        const val DB_NAME = "application-database"
    }
}