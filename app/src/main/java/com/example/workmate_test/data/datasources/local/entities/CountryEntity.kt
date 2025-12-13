package com.example.workmate_test.data.datasources.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.workmate_test.domain.models.Currency

@Entity(tableName = "country")
data class CountryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val flagUrl: String?,
    val flagAlt: String?,
    val currencies: List<Currency>,
    val capitals: List<String>,
    val languages: List<String>,
    val region: String
)