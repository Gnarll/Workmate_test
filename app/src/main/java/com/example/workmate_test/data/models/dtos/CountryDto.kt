package com.example.workmate_test.data.models.dtos


import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class CountryDto(
    @SerialName("name") val name: CountryNameDto? = null,
    @SerialName("flags") val flags: FlagsDto? = null,
    @SerialName("currencies") val currencies: Map<String, CurrencyDto>? = null,
    @SerialName("capital") val capitals: List<String>? = null,
    @SerialName("languages") val languages: Map<String, String>? = null,
    @SerialName("region") val region: String? = null
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class FlagsDto(
    @SerialName("png") val png: String?,
    @SerialName("alt") val alt: String?
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class CountryNameDto(
    @SerialName("common") val common: String?,
    @SerialName("official") val official: String?
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class CurrencyDto(
    @SerialName("name") val name: String?,
    @SerialName("symbol") val symbol: String?
)
