package com.example.workmate_test.domain.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
data class Country(
    val id: Int,
    val name: String,
    val flagUrl: String?,
    val flagAlt: String?,
    val currencies: List<Currency>,
    val capitals: List<String>,
    val languages: List<String>,
    val region: String
)

@Serializable
data class Currency(
    val name: String,
    val symbol: String
)