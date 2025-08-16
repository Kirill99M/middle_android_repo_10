package ru.yandex.buggyweatherapp

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp

private const val MEMORY_CACHE_SIZE_PERCENT = 0.25
private const val DISK_CACHE_SIZE_PERCENT = 0.02
private const val DISK_CACHE_DIR = "image_cache"

@HiltAndroidApp
class WeatherApplication : Application(), ImageLoaderFactory {

    override fun newImageLoader() = ImageLoader.Builder(applicationContext)
        .memoryCache {
            MemoryCache.Builder(applicationContext)
                .maxSizePercent(percent = MEMORY_CACHE_SIZE_PERCENT)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(directory = applicationContext.cacheDir.resolve(DISK_CACHE_DIR))
                .maxSizePercent(percent = DISK_CACHE_SIZE_PERCENT)
                .build()
        }
        .build()
}