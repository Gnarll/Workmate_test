package com.example.workmate_test.data.datasources.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workmate_test.data.datasources.local.entities.CountryEntity

@Dao
interface CountryDao {
    @Query("SELECT * FROM country ORDER BY name ASC")
    fun getCountries(): PagingSource<Int, CountryEntity>

    @Query("SELECT * FROM country WHERE name =:name")
    suspend fun getCountryByName(name: String): CountryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountries(countries: List<CountryEntity>)
}