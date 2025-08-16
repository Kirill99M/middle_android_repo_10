package ru.yandex.buggyweatherapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.yandex.buggyweatherapp.R
import ru.yandex.buggyweatherapp.viewmodel.models.UiState

@Composable
fun WeatherCard(
    state: UiState.Success,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = state.cityName,
                style = MaterialTheme.typography.headlineMedium
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                AsyncImage(
                    model = state.iconUri,
                    contentDescription = stringResource(R.string.weather_condition_image),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(50.dp)
                )
                
                Text(
                    text = stringResource(R.string.temperature, state.temperature),
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Text(
                text = stringResource(R.string.feels_like, state.feelsLike),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(
                    R.string.min_max,
                    state.minTemperature, state.maxTemperature
                ),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(
                    R.string.description,
                    state.description.replaceFirstChar { it.uppercase() }
                ),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(R.string.humidity, state.humidity),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(R.string.wind, state.wind),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = stringResource(R.string.sunrise, state.sunriseTimestamp),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = stringResource(R.string.sunset, state.sunsetTimestamp),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRefreshClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.refresh))
            }
        }
    }
}