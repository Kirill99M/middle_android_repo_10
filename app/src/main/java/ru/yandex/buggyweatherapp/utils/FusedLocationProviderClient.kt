package ru.yandex.buggyweatherapp.utils

import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@RequiresPermission(
    allOf = [
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    ]
)
suspend fun FusedLocationProviderClient.requestLastLocation(): Location? =
    suspendCoroutine { continuation ->
        lastLocation
            .addOnSuccessListener(continuation::resume)
            .addOnFailureListener(continuation::resumeWithException)
    }

@RequiresPermission(
    allOf = [
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    ]
)
suspend fun FusedLocationProviderClient.requestCurrentLocation(priority: Int): Location? =
    suspendCoroutine { continuation ->
        getCurrentLocation(priority, null)
            .addOnSuccessListener(continuation::resume)
            .addOnFailureListener(continuation::resumeWithException)
    }
