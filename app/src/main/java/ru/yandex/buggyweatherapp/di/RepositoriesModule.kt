package ru.yandex.buggyweatherapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.yandex.buggyweatherapp.repository.LocationRepository
import ru.yandex.buggyweatherapp.repository.LocationRepositoryImpl
import ru.yandex.buggyweatherapp.repository.WeatherRepository
import ru.yandex.buggyweatherapp.repository.WeatherRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoriesModule {

    @ViewModelScoped
    @Binds
    fun bindLocationRepository(repository: LocationRepositoryImpl): LocationRepository

    @ViewModelScoped
    @Binds
    fun bindWeatherRepository(repository: WeatherRepositoryImpl): WeatherRepository
}