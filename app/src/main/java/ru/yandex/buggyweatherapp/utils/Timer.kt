package ru.yandex.buggyweatherapp.utils

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.time.Duration

fun timer(interval: Duration, initialDelay: Duration = Duration.ZERO): Flow<Long> = flow {
    require(interval.isPositive())
    require(!initialDelay.isNegative())

    if (initialDelay.isPositive()) {
        delay(initialDelay)
    }

    var count = 0L

    while (currentCoroutineContext().isActive) {
        emit(count++)
        delay(interval)
    }
}