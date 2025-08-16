package ru.yandex.buggyweatherapp.repository

import com.google.gson.Gson
import ru.yandex.buggyweatherapp.api.models.WeatherResponse
import ru.yandex.buggyweatherapp.model.WeatherData

fun WeatherResponse.toDomain() = WeatherData(
    cityName = cityName.orEmpty(),
    country = sys?.country.orEmpty(),
    temperature = main?.temp ?: 0.0,
    feelsLike = main?.feelsLike ?: 0.0,
    minTemp = main?.tempMin ?: 0.0,
    maxTemp = main?.tempMax ?: 0.0,
    humidity = main?.humidity ?: 0,
    pressure = main?.pressure ?: 0,
    windSpeed = wind?.speed ?: 0.0,
    windDirection = wind?.direction ?: 0,
    description = weatherConditions?.firstOrNull()?.description.orEmpty(),
    icon = weatherConditions?.firstOrNull()?.icon,
    rain = rain?.oneHour,
    snow = snow?.oneHour,
    cloudiness = clouds?.all ?: 0,
    sunriseTime = sys?.sunrise ?: 0,
    sunsetTime = sys?.sunset ?: 0,
    timezone = timezone ?: 0,
    timestamp = dt ?: (System.currentTimeMillis() / 1000),
    rawApiData = Gson().toJson(this)
)