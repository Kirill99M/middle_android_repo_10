package ru.yandex.buggyweatherapp.repository

import android.location.Geocoder
import android.os.Build
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.yandex.buggyweatherapp.model.Location
import ru.yandex.buggyweatherapp.utils.getAddressesFromLocation
import ru.yandex.buggyweatherapp.utils.requestCurrentLocation
import ru.yandex.buggyweatherapp.utils.requestLastLocation
import java.io.IOException
import javax.inject.Inject

private const val TAG = "LocationRepository"

private const val MAX_GEOCODER_RESULTS = 1

class LocationRepositoryImpl @Inject constructor(
    private val locationProviderProvider: Lazy<FusedLocationProviderClient>,
    private val geocoder: Geocoder
) : LocationRepository {

    private val locationProvider by lazy {
        locationProviderProvider.get()
    }

    override suspend fun getCurrentLocation(): Location? = withContext(Dispatchers.IO) {
        try {
            locationProvider
                .run {
                    requestLastLocation() ?: requestCurrentLocation(Priority.PRIORITY_LOW_POWER)
                }
                ?.let { location ->
                    Location(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                }
        } catch (throwable: SecurityException) {
            Log.e(TAG, "Error getting location", throwable)
            null
        } catch (throwable: Throwable) {
            Log.e(TAG, "Location permission not granted", throwable)
            null
        }
    }

    override suspend fun getCityNameFromLocation(
        location: Location
    ): String? = withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getAddressesFromLocation(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    maxResults = MAX_GEOCODER_RESULTS
                )
            } else {
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    MAX_GEOCODER_RESULTS
                )
            }
                .orEmpty()
                .firstOrNull()
                ?.run { locality ?: subAdminArea ?: adminArea }
        } catch (throwable: IllegalArgumentException) {
            Log.e(TAG, "Invalid coordinates", throwable)
            null
        } catch (throwable: IOException) {
            Log.e(TAG, "Error getting city name", throwable)
            null
        }
    }
}