package ru.yandex.buggyweatherapp.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.yandex.buggyweatherapp.api.WeatherApiService
import ru.yandex.buggyweatherapp.di.WeatherApiKey
import ru.yandex.buggyweatherapp.model.Location
import ru.yandex.buggyweatherapp.model.WeatherData
import javax.inject.Inject

private const val TAG = "WeatherRepository"

class WeatherRepositoryImpl @Inject constructor(
    @WeatherApiKey private val apiKey: String,
    private val weatherApi: WeatherApiService
) : WeatherRepository {
    
    override suspend fun getWeatherData(
        location: Location
    ): WeatherData = withContext(Dispatchers.IO) {
        try {
            val response = weatherApi.getCurrentWeather(
                latitude = location.latitude,
                longitude = location.longitude,
                apiKey = apiKey
            )

            response.toDomain()
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error fetching weather data", throwable)
            throw throwable
        }
    }
    
    override suspend fun getWeatherByCity(
        cityName: String
    ): WeatherData = withContext(Dispatchers.IO) {
        try {
            val response = weatherApi.getWeatherByCity(
                cityName = cityName,
                apiKey = apiKey
            )

            response.toDomain()
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error fetching weather by city", throwable)
            throw throwable
        }
    }
}