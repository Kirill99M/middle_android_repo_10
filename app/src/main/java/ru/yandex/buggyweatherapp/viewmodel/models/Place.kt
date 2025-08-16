package ru.yandex.buggyweatherapp.viewmodel.models

import ru.yandex.buggyweatherapp.model.Location

sealed interface Place {

    data object Unknown : Place

    data class Coordinates(val location: Location) : Place

    data class CityName(val name: String) : Place
}