package com.example.workmate_test.presentation.viewmodels

import com.example.workmate_test.data.mappers.toCountry
import com.example.workmate_test.data.models.entitites.CountryEntityMock
import com.example.workmate_test.domain.repositiories.CountryRepository
import com.example.workmate_test.domain.utils.Result
import com.example.workmate_test.rules.MainDispatcherTestRule
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class CountryDetailsViewModelTest {
    @get:Rule
    val mainDispatcherTestRule = MainDispatcherTestRule()

    @Mock
    private lateinit var repository: CountryRepository
    private lateinit var viewModel: CountryDetailsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when repository returns Success, we have Successful state`() = runTest {
        val mockCountry = CountryEntityMock.createEntity().toCountry()
        val mockResult = Result.Success(mockCountry)

        whenever(repository.getCountry(1)).thenReturn(mockResult)
        viewModel = CountryDetailsViewModel(repository, 1)

        advanceUntilIdle()

        val state = viewModel.country.value
        assertTrue(state is Result.Success)
        assertEquals(mockCountry, (state as Result.Success).data)
    }
}