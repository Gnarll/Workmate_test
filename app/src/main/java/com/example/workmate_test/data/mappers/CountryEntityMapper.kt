package com.example.workmate_test.data.mappers

import com.example.workmate_test.data.datasources.local.entities.CountryEntity
import com.example.workmate_test.domain.models.Country

fun CountryEntity.toCountry(): Country = Country(
    id = this.id,
    name = this.name,
    flagUrl = this.flagUrl,
    flagAlt = this.flagAlt,
    currencies = this.currencies,
    capitals = this.capitals,
    languages = this.languages,
    region = this.region
)