package ru.yandex.buggyweatherapp.repository

import ru.yandex.buggyweatherapp.model.Location

interface LocationRepository {
    suspend fun getCurrentLocation(): Location?
    suspend fun getCityNameFromLocation(location: Location): String?
}