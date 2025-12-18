package com.example.workmate_test.data.datasources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workmate_test.data.models.entities.CountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Query("SELECT * FROM country ORDER BY name ASC")
    fun getCountriesFlow(): Flow<List<CountryEntity>>
 

    @Query("SELECT * FROM country WHERE id = :id")
    suspend fun getCountry(id: Int): CountryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountries(countries: List<CountryEntity>)

    @Query("DELETE FROM country")
    suspend fun deleteAllCountries()
}