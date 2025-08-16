package ru.yandex.buggyweatherapp.utils

import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val DEFAULT_MAX_RESULTS = 1

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
suspend fun Geocoder.getAddressesFromLocation(
    latitude: Double,
    longitude: Double,
    maxResults: Int = DEFAULT_MAX_RESULTS
): List<Address> = suspendCoroutine { continuation ->
    val listener = object : GeocodeListener {
        override fun onGeocode(addresses: List<Address>) {
            continuation.resume(addresses)
        }

        override fun onError(errorMessage: String?) {
            continuation.resumeWithException(IOException(errorMessage))
        }
    }

    getFromLocation(latitude, longitude, maxResults, listener)
}
