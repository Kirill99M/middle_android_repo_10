package ru.yandex.buggyweatherapp.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.yandex.buggyweatherapp.api.models.WeatherResponse

private const val WEATHER_ENDPOINT = "weather"
private const val LATITUDE_PARAM = "lat"
private const val LONGITUDE_PARAM = "lon"
private const val CITY_NAME_PARAM = "q"
private const val API_KEY_PARAM = "appid"
private const val UNITS_PARAM = "units"
private const val DEFAULT_UNITS = "metric"

interface WeatherApiService {

    @GET(WEATHER_ENDPOINT)
    suspend fun getCurrentWeather(
        @Query(LATITUDE_PARAM) latitude: Double,
        @Query(LONGITUDE_PARAM) longitude: Double,
        @Query(API_KEY_PARAM) apiKey: String,
        @Query(UNITS_PARAM) units: String = DEFAULT_UNITS
    ): WeatherResponse
    
    @GET(WEATHER_ENDPOINT)
    suspend fun getWeatherByCity(
        @Query(CITY_NAME_PARAM) cityName: String,
        @Query(API_KEY_PARAM) apiKey: String,
        @Query(UNITS_PARAM) units: String = DEFAULT_UNITS
    ): WeatherResponse
}