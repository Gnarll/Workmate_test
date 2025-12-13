package com.example.workmate_test.data.datasources.local.converters

import androidx.room.TypeConverter
import com.example.workmate_test.domain.models.Currency
import kotlinx.serialization.json.Json

class CountryConverter {
    @TypeConverter
    fun fromStringList(list: List<String>): String = Json.encodeToString(list)

    @TypeConverter
    fun toStringList(string: String): List<String> = Json.decodeFromString(string)

    @TypeConverter
    fun fromCurrencyList(list: List<Currency>): String = Json.encodeToString(list)

    @TypeConverter
    fun toCurrencyList(string: String): List<Currency> = Json.decodeFromString(string)
}