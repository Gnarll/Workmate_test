package com.example.workmate_test.data.mappers

import com.example.workmate_test.data.datasources.local.entities.CountryEntity
import com.example.workmate_test.data.models.dtos.CountryDto
import com.example.workmate_test.domain.models.Currency


fun CountryDto.toCountryEntity(): CountryEntity {
    val notAvailable = "N/A"

    val name = name?.common ?: notAvailable
    val flagUrl = flags?.png
    val flagAlt = flags?.alt
    var currencies: MutableList<Currency> = mutableListOf()

    val currencyDtos = this.currencies?.values
    currencyDtos?.forEach { currencyDto ->
        val currencyName = currencyDto.name ?: notAvailable
        val currencySymbol = currencyDto.symbol ?: notAvailable

        currencies.add(Currency(name = currencyName, symbol = currencySymbol))
    }
    currencies = currencies.ifEmpty { mutableListOf(Currency(notAvailable, notAvailable)) }

    val capitals = this.capitals ?: listOf(notAvailable)
    val languages = this.languages?.values?.map { it } ?: listOf(notAvailable)
    val region = this.region ?: notAvailable

    return CountryEntity(
        name = name,
        flagUrl = flagUrl,
        flagAlt = flagAlt,
        currencies = currencies,
        capitals = capitals,
        languages = languages,
        region = region,
    )
}