package com.example.workmate_test.data.models.entitites

import com.example.workmate_test.data.models.entities.CountryEntity
import com.example.workmate_test.domain.models.Currency

object CountryEntityMock {
    private var diff = 0
    fun createEntity(): CountryEntity = CountryEntity(
        id = diff,
        name = "Entity Name $diff",
        flagUrl = null,
        flagAlt = null,
        currencies = listOf(Currency("dollar", "$")),
        capitals = listOf("capital$diff"),
        languages = listOf("language $diff"),
        region = "Region$diff"
    ).also {
        diff++
    }
}