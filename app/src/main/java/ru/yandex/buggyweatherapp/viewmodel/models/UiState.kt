package ru.yandex.buggyweatherapp.viewmodel.models

sealed interface UiState {

    data object Loading : UiState

    data class Error(val message: Text) : UiState

    data class Success(
        val cityName: String,
        val iconUri: String?,
        val description: String,
        val temperature: Double,
        val minTemperature: Double,
        val maxTemperature: Double,
        val feelsLike: Double,
        val humidity: Int,
        val wind: Double,
        val sunriseTimestamp: String,
        val sunsetTimestamp: String
    ) : UiState
}