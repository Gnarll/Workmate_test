package com.example.workmate_test.di

import com.example.workmate_test.data.repositories.CountryRepositoryImpl
import com.example.workmate_test.domain.repositiories.CountryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CountryRepositoryModule {
    @Binds
    @Singleton
    fun bindCountryRepository(repo: CountryRepositoryImpl): CountryRepository
}