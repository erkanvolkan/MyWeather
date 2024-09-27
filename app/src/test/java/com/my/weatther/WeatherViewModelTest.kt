package com.my.weatther

import android.content.Context
import com.my.weatther.models.WeatherResponse
import com.my.weatther.repo.WeatherRepo
import com.my.weatther.viewmodels.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {

    @Mock
    lateinit var weatherRepository: WeatherRepo


    @Mock
    lateinit var context: Context

    private lateinit var viewModel: WeatherViewModel

    // Use a TestCoroutineDispatcher for controlling coroutine execution in tests
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Set the Main dispatcher to use the test dispatcher
        Dispatchers.setMain(testDispatcher)

        // Initialize the ViewModel with the mocked repository
        viewModel = WeatherViewModel(weatherRepository,context)
    }

    @After
    fun tearDown() {
        // Reset the Main dispatcher after each test
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadingState() = runTest {
        // Arrange
        val city = "New York"

        // Simulate a delay and return a response to mimic loading behavior
        `when`(weatherRepository.getData(city)).thenReturn(flow {
            emit(
                WeatherResponse(
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null
                ) // Simulating a response
            )
            kotlinx.coroutines.delay(100) // Simulate delay to mimic loading state
        })

        // Act
        viewModel.getWeather(city)

        // Assert that the initial state is INITIAL (if applicable)
        val initialState = viewModel.uiState.first()
        assertThat(initialState, instanceOf(WeatherViewModel.UiStates.INITIAL::class.java)) // Check for INITIAL state

        // Assert that the first state after calling getWeather is LOADING
        val loadingState = viewModel.uiState.drop(1).first() // Move to the next emitted state
        assertThat(loadingState, instanceOf(WeatherViewModel.UiStates.LOADING::class.java)) // Ensure LOADING state
    }


    @Test
    fun testSuccessState() = runTest {
        // Arrange
        val city = "London"
        val weatherResponse = WeatherResponse(
            null, null, null, null, null, null, null,
            null, null, null, null, null, null
        ) // Simulate a valid response
        `when`(weatherRepository.getData(city)).thenReturn(flow {
            emit(weatherResponse)
        })

        // Act
        viewModel.getWeather(city)

        // Move the dispatcher forward in time
        advanceUntilIdle()

        // Assert that the state is SUCCESS
        val uiState = viewModel.uiState.first()
        assertThat(uiState, instanceOf(WeatherViewModel.UiStates.SUCCESS::class.java)) // Check for SUCCESS type
        assertThat((uiState as WeatherViewModel.UiStates.SUCCESS).weatherResponse, `is`(weatherResponse))
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testErrorState() = runTest {
        // Arrange
        val city = "Tokyo"
        val exception = RuntimeException("Network error")
        `when`(weatherRepository.getData(city)).thenThrow(exception)

        // Act
        viewModel.getWeather(city)

        // Move the dispatcher forward in time
        advanceUntilIdle()

        // Assert that the state is ERROR
        val uiState = viewModel.uiState.first()
        assertThat(uiState, instanceOf(WeatherViewModel.UiStates.ERROR::class.java)) // Check for ERROR type
        assertThat((uiState as WeatherViewModel.UiStates.ERROR).error, `is`(exception.message))
    }
}
