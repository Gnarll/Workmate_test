package com.example.workmate_test.presentation.viewmodels

import com.example.workmate_test.data.mappers.toCountry
import com.example.workmate_test.data.models.entitites.CountryEntityMock
import com.example.workmate_test.domain.repositiories.CountryRepository
import com.example.workmate_test.domain.utils.Result
import com.example.workmate_test.rules.MainDispatcherTestRule
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals


class CountriesViewModelTest {
    @get:Rule
    val mainDispatcherTestRule = MainDispatcherTestRule()

    @Mock
    private lateinit var repository: CountryRepository
    private lateinit var viewModel: CountriesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when inits state should be Loading`() = runTest {
        viewModel = CountriesViewModel(repository)
        whenever(repository.getCountriesSteam()).thenReturn(emptyFlow())
        val state = viewModel.countriesUiState.value

        assertTrue(state.countriesResult is Result.Loading)
        assertFalse(state.isRefreshing)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when repository emits Success, should update countriesUiState`() = runTest {
        viewModel = CountriesViewModel(repository)

        val mockCountries =
            listOf(
                CountryEntityMock.createEntity(),
                CountryEntityMock.createEntity()
            ).map { it.toCountry() }

        val countriesFlow = flowOf(Result.Success(mockCountries))

        whenever(repository.getCountriesSteam()).thenReturn(countriesFlow)

        advanceUntilIdle()

        val state = viewModel.countriesUiState.value
        assertTrue(state.countriesResult is Result.Success)
        assertEquals(mockCountries, (state.countriesResult as Result.Success).data)
        assertFalse(state.isRefreshing)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onRefresh should set isRefreshing true and call refresh`() = runTest {
        viewModel = CountriesViewModel(repository)
        whenever(repository.getCountriesSteam()).thenReturn(emptyFlow())

        viewModel.onRefresh()

        val state = viewModel.countriesUiState.value
        assertTrue(state.isRefreshing)
        verify(repository).refreshCountries()
    }
}