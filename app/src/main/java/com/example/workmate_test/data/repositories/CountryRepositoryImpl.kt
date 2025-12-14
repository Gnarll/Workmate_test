package com.example.workmate_test.data.repositories

import com.example.workmate_test.data.datasources.local.dao.CountryDao
import com.example.workmate_test.data.datasources.remote.CountryApi
import com.example.workmate_test.data.mappers.toCountry
import com.example.workmate_test.data.mappers.toCountryEntity
import com.example.workmate_test.domain.models.Country
import com.example.workmate_test.domain.repositiories.CountryRepository
import com.example.workmate_test.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val countryDao: CountryDao,
    private val countryApi: CountryApi
) : CountryRepository {
    override fun getCountriesSteam(): Flow<Result<List<Country>>> {
        return countryDao.getCountries().transform { entities ->
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
            } else {
                val countries = entities.map { it.toCountry() }
                emit(Result.Success(countries))
            }

        }

    }

    override suspend fun refreshCountries() {
        countryDao.deleteAllCountries()
    }
}