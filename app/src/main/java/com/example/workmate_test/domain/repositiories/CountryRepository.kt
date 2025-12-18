package com.example.workmate_test.domain.repositiories

import com.example.workmate_test.domain.models.Country
import com.example.workmate_test.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun getCountriesStream(): Flow<Result<List<Country>>>

    suspend fun getCountry(id: Int): Result<Country>
    suspend fun refreshCountries()
}