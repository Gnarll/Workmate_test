package com.example.workmate_test.data.repositories

import com.example.workmate_test.data.datasources.local.dao.CountryDao
import com.example.workmate_test.data.datasources.remote.CountryApi
import com.example.workmate_test.data.mappers.toCountry
import com.example.workmate_test.data.mappers.toCountryEntity
import com.example.workmate_test.data.utils.RefreshState
import com.example.workmate_test.data.utils.ResultWrapper
import com.example.workmate_test.domain.models.Country
import com.example.workmate_test.domain.repositiories.CountryRepository
import com.example.workmate_test.domain.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val countryDao: CountryDao,
    private val countryApi: CountryApi
) : CountryRepository {

    private val refreshFlow: MutableSharedFlow<RefreshState> =
        MutableSharedFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCountriesCollectingFlow(): Flow<ResultWrapper<Result<List<Country>>>> =
        refreshFlow.onStart { emit(RefreshState.INITIAL) }
            .flatMapLatest { refreshState ->
                val openDbChannel = Channel<Boolean>()

                channelFlow {
                    launch {
                        when (refreshState) {
                            RefreshState.INITIAL -> {
                                openDbChannel.send(true)
                            }

                            RefreshState.DO_REFRESH -> {
                                try {
                                    val newCountryEntities =
                                        countryApi.getCountries().map { it.toCountryEntity() }

                                    countryDao.deleteAllCountries()
                                    countryDao.insertCountries(newCountryEntities)
                                } catch (e: Throwable) {
                                    val customError = Result.Error.convertToCustomError(e)
                                    send(ResultWrapper.Existent(customError))
                                }
                                openDbChannel.send(true)
                            }
                        }
                    }

                    launch {
                        send(ResultWrapper.Existent(Result.Loading))

                        if (openDbChannel.receive()) {
                            countryDao.getCountriesFlow().collect { entities ->
                                if (entities.isEmpty()) {
                                    send(ResultWrapper.Empty)
                                } else {
                                    val countries = entities.map { it.toCountry() }
                                    send(ResultWrapper.Existent(Result.Success(countries)))
                                }
                            }
                        }
                    }

                }
            }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCountriesStream(): Flow<Result<List<Country>>> =
        getCountriesCollectingFlow().transform { updateState ->
            when (updateState) {
                ResultWrapper.Empty -> {
                    try {
                        val newCountryEntities =
                            countryApi.getCountries().map { it.toCountryEntity() }
                        countryDao.insertCountries(newCountryEntities)

                    } catch (e: Throwable) {
                        val customError = Result.Error.convertToCustomError(e)
                        emit(customError)
                    }
                }

                is ResultWrapper.Existent -> {
                    emit(updateState.result)
                }
            }
        }.distinctUntilChanged()

    override suspend fun refreshCountries() {
        refreshFlow.emit(RefreshState.DO_REFRESH)
    }

    override suspend fun getCountry(id: Int): Result<Country> {
        return try {
            Result.Success(countryDao.getCountry(id).toCountry())
        } catch (e: Throwable) {
            Result.Error.UnknownError(e)
        }
    }

}