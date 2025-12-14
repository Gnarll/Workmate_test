package com.example.workmate_test.data.datasources.remote

import com.example.workmate_test.data.models.dtos.CountryDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CountryApi {
    @GET("all")
    suspend fun getCountries(
        @Query("fields") fields: List<String> = listOf(
            "name",
            "flags",
            "currencies",
            "capital",
            "languages",
            "region"
        )
    ): List<CountryDto>
}
