package com.example.workmate_test.data.repositories

import com.example.workmate_test.data.datasources.local.dao.CountryDao
import com.example.workmate_test.data.datasources.remote.CountryApi
import com.example.workmate_test.data.mappers.toCountry
import com.example.workmate_test.data.mappers.toCountryEntity
import com.example.workmate_test.domain.models.Country
import com.example.workmate_test.domain.repositiories.CountryRepository
import com.example.workmate_test.domain.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val countryDao: CountryDao,
    private val countryApi: CountryApi
) : CountryRepository {
    private val refreshTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCountriesSteam(): Flow<Result<List<Country>>> {
        return refreshTrigger.flatMapLatest {
            countryDao.getCountries().transformLatest { entities ->
                emit(Result.Loading)

                if (entities.isEmpty()) {
                    try {
                        val countryEntities = countryApi.getCountries()
                            .map { it.toCountryEntity() }
                        countryDao.insertCountries(countryEntities)
                    } catch (e: IOException) {
                        emit(Result.Error.NetworkError(e))
                    } catch (e: HttpException) {
                        emit(Result.Error.ServerError(e))
                    } catch (e: SerializationException) {
                        emit(Result.Error.SerializationError(e))
                    } catch (e: Throwable) {
                        emit(Result.Error.UnknownError(e))
                    }
                }

                if (entities.isNotEmpty()) {
                    val countries = entities.map { it.toCountry() }
                    emit(Result.Success(countries))
                }
            }
        }
            .distinctUntilChanged()
    }

    override suspend fun refreshCountries() {
        countryDao.deleteAllCountries()
        refreshTrigger.update { it + 1 }
    }

    override suspend fun getCountry(id: Int): Result<Country> {
        return try {
            Result.Success(countryDao.getCountry(id).toCountry())
        } catch (e: Throwable) {
            Result.Error.UnknownError(e)
        }
    }

}