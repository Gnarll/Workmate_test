package com.example.workmate_test.data.models.dtos

object CountryDtoMock {
    private var diff = 0
    fun createDto(): CountryDto = CountryDto(
        name = CountryNameDto(common = "Dto Common name $diff", official = "Official name $diff"),
        flags = FlagsDto(png = null, alt = null),
        currencies = mapOf("$diff" to CurrencyDto(name = "dollar", symbol = "$")),
        capitals = listOf("capital$diff"),
        languages = mapOf("en" to "English"),
        region = "Region$diff"
    ).also { diff++ }
}