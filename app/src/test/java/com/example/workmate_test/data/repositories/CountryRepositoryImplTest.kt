package com.example.workmate_test.data.repositories

import app.cash.turbine.test
import com.example.workmate_test.data.datasources.local.dao.CountryDao
import com.example.workmate_test.data.datasources.remote.CountryApi
import com.example.workmate_test.data.mappers.toCountry
import com.example.workmate_test.data.mappers.toCountryEntity
import com.example.workmate_test.data.models.dtos.CountryDtoMock
import com.example.workmate_test.data.models.entities.CountryEntity
import com.example.workmate_test.data.models.entitites.CountryEntityMock
import com.example.workmate_test.domain.utils.Result
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException
import kotlin.test.assertEquals

class CountryRepositoryImplTest {

    @Mock
    private lateinit var countryDao: CountryDao

    @Mock
    private lateinit var countryApi: CountryApi

    lateinit var countryRepositoryImpl: CountryRepositoryImpl


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        countryRepositoryImpl =
            CountryRepositoryImpl(countryDao = countryDao, countryApi = countryApi)
    }


    @Test
    fun `when DB has data, should emit Loading then Success`() = runTest {
        val mockEntities = listOf(
            CountryEntityMock.createEntity()
        )
        val expectedCountries = mockEntities.map { it.toCountry() }

        whenever(countryDao.getCountries()).thenReturn(flowOf(mockEntities))

        countryRepositoryImpl.getCountriesSteam().test {
            assert(awaitItem() is Result.Loading)
            val success = awaitItem()
            assertTrue(success is Result.Success && success.data == expectedCountries)
        }
    }

    @Test
    fun `when DB has no data, should call Api, insert mock dto, return Success`() = runTest {
        val mockDtos = listOf(CountryDtoMock.createDto())
        val mockEntities = mockDtos.map { it.toCountryEntity() }
        val expectedCountries = mockEntities.map { it.toCountry() }

        val daoFlow: MutableSharedFlow<List<CountryEntity>> =
            MutableSharedFlow()

        whenever(countryApi.getCountries()).thenReturn(mockDtos)
        whenever(countryDao.getCountries()).thenReturn(daoFlow)



        countryRepositoryImpl.getCountriesSteam().test {
            daoFlow.emit(emptyList())
            assert(awaitItem() is Result.Loading)

            verify(countryDao).insertCountries(mockEntities)

            daoFlow.emit(mockEntities)
            val success = awaitItem()
            assert(success is Result.Success && success.data == expectedCountries)
        }
    }

    @Test
    fun `when API throws IOException, should emit NetworkError`() = runTest {
        val entitiesFlow = MutableSharedFlow<List<CountryEntity>>()

        whenever(countryDao.getCountries()).thenReturn(entitiesFlow)
        whenever(countryApi.getCountries()).thenAnswer { throw IOException("No Internet") }

        countryRepositoryImpl.getCountriesSteam().test {
            entitiesFlow.emit(emptyList())
            assert(awaitItem() is Result.Loading)
            val error = awaitItem()
            assertTrue(error is Result.Error.NetworkError)
        }
    }

    @Test
    fun `when refreshed, should flush db and fetch data again`() = runTest {
        val oldEntities = listOf(CountryEntityMock.createEntity(), CountryEntityMock.createEntity())

        val newDtos = listOf(CountryDtoMock.createDto())
        val newEntities = newDtos.map { it.toCountryEntity() }
        val expectedCountries = newEntities.map { it.toCountry() }

        val entitiesFlow = MutableStateFlow(oldEntities)

        whenever(countryDao.getCountries()).thenReturn(entitiesFlow)
        whenever(countryApi.getCountries()).thenReturn(newDtos)
        whenever(countryDao.insertCountries(newEntities)).doAnswer {
            entitiesFlow.value = newEntities
        }
        whenever(countryDao.deleteAllCountries()).doAnswer {
            entitiesFlow.value = emptyList()
        }


        countryRepositoryImpl.getCountriesSteam().test {
            assertEquals(awaitItem(), Result.Loading)
            assertEquals(awaitItem(), Result.Success(oldEntities.map { it.toCountry() }))

            countryRepositoryImpl.refreshCountries()
            verify(countryDao).deleteAllCountries()
            assertEquals(awaitItem(), Result.Loading)

            val successNewCountries = awaitItem()
            assertTrue(
                successNewCountries is Result.Success
                        && successNewCountries.data == expectedCountries
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}
