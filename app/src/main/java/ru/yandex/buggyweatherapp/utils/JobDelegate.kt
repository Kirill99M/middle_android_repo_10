package ru.yandex.buggyweatherapp.utils

import kotlinx.coroutines.Job
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class JobDelegate : ReadWriteProperty<Any?, Job?> {
    private var job: Job? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): Job? = job

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Job?) {
        job?.cancel()
        job = value
    }
}

fun job() = JobDelegate()