package com.min.weatherapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import com.min.weatherapp.data.model.SavedLocation
import com.min.weatherapp.util.LocaleHelper
import android.app.Activity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.min.weatherapp.R
import com.min.weatherapp.data.model.WeatherResponse
import com.min.weatherapp.viewmodel.WeatherUiState
import com.min.weatherapp.viewmodel.WeatherViewModel
import com.min.weatherapp.util.WeatherUtils
import com.min.weatherapp.util.capitalizeWords

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var cityName by remember { mutableStateOf("") }
    var isDrawerOpen by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val currentLocale = LocalConfiguration.current.locales[0]
    var selectedLanguage by remember { mutableStateOf(if (currentLocale.language == "vi") "vi" else "en") }
    val savedLocations = remember { mutableStateListOf<SavedLocation>() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                val drawerContext = LocalContext.current
                Spacer(Modifier.height(16.dp))
                Text(
                    stringResource(R.string.drawer_saved_locations),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                Divider()

                if (savedLocations.isEmpty()) {
                    Text(
                        text = stringResource(R.string.drawer_no_saved_locations),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    savedLocations.forEach { location ->
                        ListItem(
                            headlineContent = { Text(location.city) },
                            supportingContent = { Text(location.country) },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null
                                )
                            },
                            trailingContent = {
                                IconButton(onClick = { savedLocations.remove(location) }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = stringResource(R.string.drawer_remove)
                                    )
                                }
                            },
                            modifier = Modifier.clickable {
                                viewModel.getWeatherByCity(location.city)
                                scope.launch { drawerState.close() }
                            }
                        )
                    }
                }

                Divider(Modifier.padding(vertical = 16.dp))

                // Language Selection
                Text(
                    stringResource(R.string.drawer_language),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                ListItem(
                    headlineContent = { Text(stringResource(R.string.drawer_language_english)) },
                    leadingContent = {
                        RadioButton(
                            selected = selectedLanguage == "en",
                            onClick = {
                                selectedLanguage = "en"
                                LocaleHelper.setLocale(drawerContext, "en")
                                (drawerContext as? Activity)?.recreate()
                            }
                        )
                    }
                )
                ListItem(
                    headlineContent = { Text(stringResource(R.string.drawer_language_vietnamese)) },
                    leadingContent = {
                        RadioButton(
                            selected = selectedLanguage == "vi",
                            onClick = {
                                selectedLanguage = "vi"
                                LocaleHelper.setLocale(drawerContext, "vi")
                                (drawerContext as? Activity)?.recreate()
                            }
                        )
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.app_name),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        if (uiState is WeatherUiState.Success) {
                            IconButton(onClick = {
                                val weather = (uiState as WeatherUiState.Success).weather
                                val newLocation = SavedLocation(
                                    city = weather.name,
                                    country = weather.sys.country
                                )
                                if (!savedLocations.contains(newLocation)) {
                                    savedLocations.add(newLocation)
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = stringResource(R.string.drawer_save_current)
                                )
                            }
                        }
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
                    CitySearchBar(
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
    fun CitySearchBar(
        cityName: String,
        onCityNameChange: (String) -> Unit,
        onSearch: () -> Unit
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        OutlinedTextField(
            value = cityName,
            onValueChange = onCityNameChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            placeholder = { Text(stringResource(R.string.search_hint)) },
            label = { Text(stringResource(R.string.search_label)) },
            trailingIcon = {
                Row {
                    if (cityName.isNotBlank()) {
                        IconButton(onClick = { onCityNameChange("") }) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(
                        onClick = onSearch,
                        enabled = cityName.isNotBlank()
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = if (cityName.isNotBlank())
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (cityName.isNotBlank()) {
                        keyboardController?.hide()
                        onSearch()
                    }
                }
            ),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            supportingText = {
                if (cityName.isNotBlank() && cityName.length < 2) {
                    Text(
                        stringResource(R.string.search_error_min_length),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        )
    }

    @Composable
    fun IdleContent() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🌤️",
                fontSize = 80.sp,
                modifier = Modifier.padding(vertical = 32.dp)
            )
            Text(
                text = stringResource(R.string.welcome_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.welcome_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Popular cities suggestions
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.popular_cities_title),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.popular_cities_list),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
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
                text = stringResource(R.string.loading),
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
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                    text = stringResource(R.string.error_title),
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
                Spacer(modifier = Modifier.height(16.dp))

                // Troubleshooting tips
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "💡 ${stringResource(R.string.error_tips_title)}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.error_tips_content),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
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
                text = WeatherUtils.formatDate(weather.dt),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Last updated timestamp
            Text(
                text = stringResource(R.string.last_updated, WeatherUtils.formatTime(weather.dt)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )

            // Main weather icon and description
            if (weather.weather.isNotEmpty()) {
                Text(
                    text = WeatherUtils.getWeatherEmoji(weather.weather[0].main),
                    fontSize = 100.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = weather.weather[0].description.capitalizeWords(),
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
                text = stringResource(R.string.feels_like, weather.main.feelsLike.toInt()),
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
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    WeatherDetailRow(
                        stringResource(R.string.min_max_temp),
                        stringResource(
                            R.string.min_max_temp_value,
                            weather.main.tempMin.toInt(),
                            weather.main.tempMax.toInt()
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    WeatherDetailRow(
                        stringResource(R.string.humidity),
                        stringResource(
                            R.string.humidity_value,
                            weather.main.humidity,
                            WeatherUtils.getHumidityDescription(weather.main.humidity)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    WeatherDetailRow(
                        stringResource(R.string.pressure),
                        stringResource(R.string.pressure_value, weather.main.pressure)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    WeatherDetailRow(
                        stringResource(R.string.wind),
                        stringResource(
                            R.string.wind_value,
                            weather.wind.speed,
                            WeatherUtils.getWindDirection(weather.wind.deg)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    WeatherDetailRow(
                        stringResource(R.string.visibility),
                        stringResource(R.string.visibility_value, weather.visibility / 1000)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    WeatherDetailRow(
                        stringResource(R.string.cloudiness),
                        stringResource(R.string.cloudiness_value, weather.clouds.all)
                    )
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
                SunTimeCard("Sunrise", WeatherUtils.formatTime(weather.sys.sunrise), "🌅")
                SunTimeCard("Sunset", WeatherUtils.formatTime(weather.sys.sunset), "🌇")
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
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
}
