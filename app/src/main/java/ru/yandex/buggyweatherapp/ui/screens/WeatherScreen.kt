package ru.yandex.buggyweatherapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.yandex.buggyweatherapp.R
import ru.yandex.buggyweatherapp.ui.components.LocationSearch
import ru.yandex.buggyweatherapp.ui.components.RequestLocationPermissionsEffect
import ru.yandex.buggyweatherapp.ui.components.WeatherCard
import ru.yandex.buggyweatherapp.viewmodel.WeatherViewModel
import ru.yandex.buggyweatherapp.viewmodel.models.Text
import ru.yandex.buggyweatherapp.viewmodel.models.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel(), modifier: Modifier = Modifier) {
    var requestLocationKey by remember { mutableIntStateOf(0) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    RequestLocationPermissionsEffect(
        key = requestLocationKey,
        onGranted = viewModel::fetchCurrentLocationWeather,
        onDenied = viewModel::locationPermissionDenied
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LocationSearch(
            onCitySearch = viewModel::fetchWeatherByCity,
            onLocationRequest = { requestLocationKey++ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = state) {
            is UiState.Loading -> {
                Text(stringResource(R.string.loading))
            }

            is UiState.Error -> {
                Text(
                    text = state.message.asString(),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }

            is UiState.Success -> {
                WeatherCard(
                    state = state,
                    onRefreshClick = viewModel::refresh
                )
            }
        }
    }
}

@Composable
fun Text.asString(): String {
    return when (this) {
        is Text.Value -> value
        is Text.Resource -> stringResource(resId)
    }
}