package ru.yandex.buggyweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yandex.buggyweatherapp.R
import ru.yandex.buggyweatherapp.model.Location
import ru.yandex.buggyweatherapp.model.WeatherData
import ru.yandex.buggyweatherapp.repository.LocationRepository
import ru.yandex.buggyweatherapp.repository.WeatherRepository
import ru.yandex.buggyweatherapp.utils.job
import ru.yandex.buggyweatherapp.utils.timer
import ru.yandex.buggyweatherapp.viewmodel.models.Place
import ru.yandex.buggyweatherapp.viewmodel.models.Text
import ru.yandex.buggyweatherapp.viewmodel.models.UiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

private const val REFRESH_INTERVAL_MIN = 1
private const val MILLISECONDS_IN_SECOND = 1000L
private const val WEATHER_ICON_URI_PATTERN = "https://openweathermap.org/img/wn/%s@2x.png"
private const val TIMESTAMP_FORMAT = "HH:mm"

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    private var getWeatherJob by job()

    private var place = MutableStateFlow<Place>(Place.Unknown)
    private val _state = MutableStateFlow<UiState>(UiState.Loading)

    val state = _state.asStateFlow()

    init {
        startAutoRefresh()
    }

    private fun startAutoRefresh() {
        timer(interval = REFRESH_INTERVAL_MIN.minutes)
            .onEach { refresh() }
            .launchIn(viewModelScope)
    }

    fun refresh() {
        when (val place = place.value) {
            is Place.Coordinates -> {
                getWeatherByLocation(place.location)
            }

            is Place.CityName -> {
                getWeatherByCity(place.name)
            }

            else -> { /* no-op */ }
        }
    }

    fun locationPermissionDenied() {
        _state.value = UiState.Error(Text.Resource(R.string.location_error))
    }

    fun fetchCurrentLocationWeather() {
        viewModelScope.launch {
            _state.update { state ->
                when (state) {
                    is UiState.Error -> UiState.Loading
                    else -> state
                }
            }

            val location = locationRepository.getCurrentLocation()

            when (location) {
                null -> _state.value = UiState.Error(Text.Resource(R.string.location_error))
                else -> getWeatherByLocation(location)
            }
        }
    }
    
    private fun getWeatherByLocation(location: Location) {
        getWeatherJob = viewModelScope.launch {
            place.value = Place.Coordinates(location)

            _state.update { state ->
                when (state) {
                    is UiState.Error -> UiState.Loading
                    else -> state
                }
            }

            try {
                val weatherData = weatherRepository.getWeatherData(location)
                val cityName = locationRepository.getCityNameFromLocation(location)

                _state.value = mapToUiState(weatherData, cityName)
            } catch (throwable: Throwable) {
                val errorMessage = throwable.message
                    ?.let(Text::Value)
                    ?: Text.Resource(R.string.network_error)

                _state.value = UiState.Error(errorMessage)
            }
        }
    }

    fun fetchWeatherByCity(city: String) {
        val cityName = city.trim()

        if (cityName.isBlank()) {
            _state.value = UiState.Error(Text.Resource(R.string.empty_city_name_error))
            return
        }

        getWeatherByCity(cityName)
    }

    private fun getWeatherByCity(city: String) {
        getWeatherJob = viewModelScope.launch {
            place.value = Place.CityName(city)

            _state.update { state ->
                when (state) {
                    is UiState.Error -> UiState.Loading
                    else -> state
                }
            }

            try {
                val data = weatherRepository.getWeatherByCity(city)
                _state.value = mapToUiState(data, city)
            } catch (throwable: Throwable) {
                val errorMessage = throwable.message
                    ?.let(Text::Value)
                    ?: Text.Resource(R.string.network_error)

                _state.value = UiState.Error(errorMessage)
            }
        }
    }

    private fun mapToUiState(weatherData: WeatherData, cityName: String?) = UiState.Success(
        cityName = cityName ?: weatherData.cityName,
        iconUri = weatherData.icon?.let { WEATHER_ICON_URI_PATTERN.format(it) },
        description = weatherData.description,
        temperature = weatherData.temperature,
        minTemperature = weatherData.minTemp,
        maxTemperature = weatherData.maxTemp,
        feelsLike = weatherData.feelsLike,
        humidity = weatherData.humidity,
        wind = weatherData.windSpeed,
        sunriseTimestamp = formatTimestamp(weatherData.sunriseTime),
        sunsetTimestamp = formatTimestamp(weatherData.sunsetTime)
    )

    private fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp * MILLISECONDS_IN_SECOND)
        val formatter = SimpleDateFormat(TIMESTAMP_FORMAT, Locale.getDefault())
        return formatter.format(date)
    }
}