package com.min.weatherapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.min.weatherapp.data.model.WeatherResponse
import com.min.weatherapp.viewmodel.WeatherUiState
import com.min.weatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var cityName by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Weather App",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Search bar
                SearchBar(
                    cityName = cityName,
                    onCityNameChange = { cityName = it },
                    onSearch = { 
                        if (cityName.isNotBlank()) {
                            viewModel.getWeatherByCity(cityName)
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Content based on state
                when (val state = uiState) {
                    is WeatherUiState.Idle -> {
                        IdleContent()
                    }
                    is WeatherUiState.Loading -> {
                        LoadingContent()
                    }
                    is WeatherUiState.Success -> {
                        WeatherContent(weather = state.weather)
                    }
                    is WeatherUiState.Error -> {
                        ErrorContent(message = state.message)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    cityName: String,
    onCityNameChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = cityName,
        onValueChange = onCityNameChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        placeholder = { Text("Enter city name") },
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun IdleContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🌤️",
            fontSize = 80.sp,
            modifier = Modifier.padding(32.dp)
        )
        Text(
            text = "Search for a city to see weather",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Loading weather data...",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ErrorContent(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚠️",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Error",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun WeatherContent(weather: WeatherResponse) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // City name and country
        Text(
            text = "${weather.name}, ${weather.sys.country}",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Date
        Text(
            text = formatDate(weather.dt),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Main weather icon and description
        if (weather.weather.isNotEmpty()) {
            Text(
                text = getWeatherEmoji(weather.weather[0].main),
                fontSize = 100.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = weather.weather[0].description.replaceFirstChar { 
                    if (it.isLowerCase()) it.titlecase() else it.toString() 
                },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Temperature
        Text(
            text = "${weather.main.temp.toInt()}°C",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 72.sp
        )
        
        Text(
            text = "Feels like ${weather.main.feelsLike.toInt()}°C",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Weather details card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                WeatherDetailRow("Min/Max", "${weather.main.tempMin.toInt()}°C / ${weather.main.tempMax.toInt()}°C")
                Spacer(modifier = Modifier.height(12.dp))
                WeatherDetailRow("Humidity", "${weather.main.humidity}%")
                Spacer(modifier = Modifier.height(12.dp))
                WeatherDetailRow("Pressure", "${weather.main.pressure} hPa")
                Spacer(modifier = Modifier.height(12.dp))
                WeatherDetailRow("Wind Speed", "${weather.wind.speed} m/s")
                Spacer(modifier = Modifier.height(12.dp))
                WeatherDetailRow("Visibility", "${weather.visibility / 1000} km")
                Spacer(modifier = Modifier.height(12.dp))
                WeatherDetailRow("Cloudiness", "${weather.clouds.all}%")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Sunrise and Sunset
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SunTimeCard("Sunrise", formatTime(weather.sys.sunrise), "🌅")
            SunTimeCard("Sunset", formatTime(weather.sys.sunset), "🌇")
        }
    }
}

@Composable
fun WeatherDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SunTimeCard(label: String, time: String, emoji: String) {
    Card(
        modifier = Modifier
            .width(160.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = time,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp * 1000))
}

fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp * 1000))
}

fun getWeatherEmoji(weatherMain: String): String {
    return when (weatherMain.lowercase()) {
        "clear" -> "☀️"
        "clouds" -> "☁️"
        "rain" -> "🌧️"
        "drizzle" -> "🌦️"
        "thunderstorm" -> "⛈️"
        "snow" -> "❄️"
        "mist", "fog" -> "🌫️"
        "haze" -> "🌫️"
        "smoke" -> "💨"
        else -> "🌤️"
    }
}
