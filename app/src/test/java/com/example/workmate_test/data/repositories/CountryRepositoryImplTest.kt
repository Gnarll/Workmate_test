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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

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

        whenever(countryDao.getCountriesFlow()).thenReturn(flowOf(mockEntities))

        countryRepositoryImpl.getCountriesStream().test {
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

        val daoFlow: MutableStateFlow<List<CountryEntity>> =
            MutableStateFlow(emptyList())

        whenever(countryDao.getCountriesFlow()).thenReturn(daoFlow)
        whenever(countryDao.insertCountries(any<List<CountryEntity>>()))
            .then { invocation ->
                val countries = invocation.getArgument<List<CountryEntity>>(0)
                daoFlow.value = countries
            }

        whenever(countryApi.getCountries()).thenReturn(mockDtos)



        countryRepositoryImpl.getCountriesStream().test {
            assert(awaitItem() is Result.Loading)

            verify(countryDao).insertCountries(mockEntities)

            val success = awaitItem()
            assert(success is Result.Success && success.data == expectedCountries)
        }
    }

    @Test
    fun `when API throws IOException, should emit NetworkError`() = runTest {
        whenever(countryDao.getCountriesFlow()).thenReturn(flowOf(emptyList()))
        whenever(countryApi.getCountries()).thenAnswer {
            throw IOException("No Internet")
        }

        countryRepositoryImpl.getCountriesStream().test {
            assert(awaitItem() is Result.Loading)
            val error = awaitItem()
            assertTrue(error is Result.Error.NetworkError)
        }
    }

    @Test
    fun `when db has data, and refresh triggered, should emit Loading - Success - Loading - Success`() =
        runTest {
            val initialDataEntities =
                listOf(CountryEntityMock.createEntity(), CountryEntityMock.createEntity())
            val expectedInitialData = initialDataEntities.map { it.toCountry() }

            val newDataDtos = listOf(CountryDtoMock.createDto())
            val expectedNewData = newDataDtos.map { it.toCountryEntity().toCountry() }

            val daoFlow: MutableStateFlow<List<CountryEntity>> =
                MutableStateFlow(initialDataEntities)

            whenever(countryDao.getCountriesFlow()).thenReturn(daoFlow)
            whenever(countryDao.insertCountries(any<List<CountryEntity>>()))
                .then { invocation ->
                    val countries = invocation.getArgument<List<CountryEntity>>(0)
                    daoFlow.value = countries
                }
            whenever(countryDao.deleteAllCountries()).then {
                daoFlow.value = emptyList()
            }
            whenever(countryApi.getCountries()).thenReturn(newDataDtos)


            countryRepositoryImpl.getCountriesStream().test {

                assertTrue(awaitItem() is Result.Loading)

                val successInitialData = awaitItem()
                assertTrue(successInitialData is Result.Success && expectedInitialData == successInitialData.data)

                countryRepositoryImpl.refreshCountries()

                assertTrue(awaitItem() is Result.Loading)

                val successNewData = awaitItem()
                assertTrue(successNewData is Result.Success && expectedNewData == successNewData.data)
            }
        }
}