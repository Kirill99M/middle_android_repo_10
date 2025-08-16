package ru.yandex.buggyweatherapp.api.models

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord") val coord: CoordResponse?,
    @SerializedName("weather") val weatherConditions: List<WeatherConditionResponse>?,
    @SerializedName("main") val main: MainResponse?,
    @SerializedName("wind") val wind: WindResponse?,
    @SerializedName("rain") val rain: PrecipitationResponse?,
    @SerializedName("snow") val snow: PrecipitationResponse?,
    @SerializedName("clouds") val clouds: CloudsResponse?,
    @SerializedName("dt") val dt: Long?,
    @SerializedName("sys") val sys: SysResponse?,
    @SerializedName("timezone") val timezone: Int?,
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val cityName: String?,
    @SerializedName("cod") val cod: Int?
)

data class CoordResponse(
    @SerializedName("lon") val lon: Double?,
    @SerializedName("lat") val lat: Double?
)

data class WeatherConditionResponse(
    @SerializedName("description") val description: String?,
    @SerializedName("icon") val icon: String?
)

data class MainResponse(
    @SerializedName("temp") val temp: Double?,
    @SerializedName("feels_like") val feelsLike: Double?,
    @SerializedName("temp_min") val tempMin: Double?,
    @SerializedName("temp_max") val tempMax: Double?,
    @SerializedName("pressure") val pressure: Int?,
    @SerializedName("humidity") val humidity: Int?
)

data class WindResponse(
    @SerializedName("speed") val speed: Double?,
    @SerializedName("deg") val direction: Int?,
)

data class PrecipitationResponse(
    @SerializedName("1h") val oneHour: Double?
)

data class CloudsResponse(
    @SerializedName("all") val all: Int?
)

data class SysResponse(
    @SerializedName("country") val country: String?,
    @SerializedName("sunrise") val sunrise: Long?,
    @SerializedName("sunset") val sunset: Long?
)