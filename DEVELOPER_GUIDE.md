# Developer Guide - Weather App

## Project Structure

### Package Organization

```
com.min.weatherapp/
├── data/                           # Data layer
│   ├── api/                        # API services and Retrofit setup
│   │   ├── WeatherApiService.kt    # API endpoints definition
│   │   └── RetrofitInstance.kt     # Retrofit singleton
│   ├── model/                      # Data models
│   │   └── WeatherData.kt          # Weather response models
│   └── repository/                 # Repository pattern
│       └── WeatherRepository.kt    # Data repository
├── ui/                             # UI layer
│   ├── screens/                    # Screen composables
│   │   └── WeatherScreen.kt        # Main weather screen
│   └── theme/                      # App theming
│       ├── Color.kt                # Color definitions
│       ├── Theme.kt                # Theme configuration
│       └── Type.kt                 # Typography
├── viewmodel/                      # ViewModel layer
│   └── WeatherViewModel.kt         # Main view model
└── MainActivity.kt                 # Entry point
```

## Key Components

### 1. Data Layer

#### WeatherApiService
Defines the API endpoints using Retrofit:
- `getCurrentWeather(city, apiKey, units)` - Fetch weather by city name
- `getCurrentWeatherByCoordinates(lat, lon, apiKey, units)` - Fetch weather by coordinates

#### RetrofitInstance
Singleton object that provides:
- OkHttp client with logging interceptor
- Retrofit instance with Gson converter
- Base URL configuration

#### WeatherRepository
Manages data operations:
- Wraps API calls in Result<T> for better error handling
- Handles exceptions and converts them to Result.failure
- Single source of truth for weather data

### 2. ViewModel Layer

#### WeatherViewModel
- Manages UI state using StateFlow
- Exposes `uiState: StateFlow<WeatherUiState>`
- Provides methods to fetch weather by city or coordinates
- Handles loading states and errors

#### WeatherUiState (Sealed Class)
```kotlin
sealed class WeatherUiState {
    object Idle : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val weather: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
```

### 3. UI Layer

#### WeatherScreen
Main composable that displays:
- Search bar for city input
- Different UI states (Idle, Loading, Success, Error)
- Weather details with Material 3 components

#### UI Components
- `SearchBar` - Input field with search icon
- `IdleContent` - Initial state UI
- `LoadingContent` - Loading indicator
- `ErrorContent` - Error message display
- `WeatherContent` - Main weather information
- `WeatherDetailRow` - Individual weather detail
- `SunTimeCard` - Sunrise/sunset cards

## State Management

The app uses Kotlin Flow for state management:

```kotlin
// In ViewModel
private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

// In Composable
val uiState by viewModel.uiState.collectAsState()
```

## API Integration

### OpenWeatherMap API

**Base URL:** `https://api.openweathermap.org/data/2.5/`

**Endpoint:** `/weather`

**Parameters:**
- `q` (String) - City name (e.g., "London", "New York")
- `lat` (Double) - Latitude for coordinate-based search
- `lon` (Double) - Longitude for coordinate-based search
- `appid` (String) - Your API key
- `units` (String) - "metric", "imperial", or "standard" (default: metric)

**Response Format:**
```json
{
  "coord": { "lon": -0.1257, "lat": 51.5085 },
  "weather": [
    {
      "id": 800,
      "main": "Clear",
      "description": "clear sky",
      "icon": "01d"
    }
  ],
  "main": {
    "temp": 15.5,
    "feels_like": 14.2,
    "temp_min": 14.0,
    "temp_max": 17.0,
    "pressure": 1013,
    "humidity": 72
  },
  "wind": { "speed": 3.6, "deg": 270 },
  "clouds": { "all": 0 },
  "sys": {
    "country": "GB",
    "sunrise": 1697702400,
    "sunset": 1697742000
  },
  "name": "London"
}
```

## Adding New Features

### Adding a New Weather Detail

1. Update the UI in `WeatherScreen.kt`:
```kotlin
WeatherDetailRow("New Detail", "${weather.newField}")
```

2. Ensure the field exists in `WeatherData.kt` models

### Adding Location-Based Weather

1. Add location permissions to `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

2. Request permissions in MainActivity

3. Use `viewModel.getWeatherByCoordinates(lat, lon)` with obtained coordinates

### Adding Weather Forecast

1. Add new API endpoint in `WeatherApiService.kt`:
```kotlin
@GET("forecast")
suspend fun getForecast(
    @Query("q") city: String,
    @Query("appid") apiKey: String,
    @Query("units") units: String = "metric"
): ForecastResponse
```

2. Create `ForecastResponse` data model

3. Add forecast method in repository and viewmodel

4. Create new UI screen for forecast display

## Testing

### Unit Tests
Location: `app/src/test/java/com/min/weatherapp/`

Example test for ViewModel:
```kotlin
@Test
fun `getWeatherByCity updates state to Success on successful API call`() = runTest {
    // Arrange
    val mockRepository = mockk<WeatherRepository>()
    val viewModel = WeatherViewModel(mockRepository)
    
    // Act
    viewModel.getWeatherByCity("London")
    
    // Assert
    assertTrue(viewModel.uiState.value is WeatherUiState.Success)
}
```

### Instrumented Tests
Location: `app/src/androidTest/java/com/min/weatherapp/`

Example UI test:
```kotlin
@Test
fun searchBar_displaysCorrectPlaceholder() {
    composeTestRule.setContent {
        WeatherScreen(viewModel = WeatherViewModel())
    }
    
    composeTestRule
        .onNodeWithText("Enter city name")
        .assertIsDisplayed()
}
```

## Performance Considerations

1. **API Caching**: Consider implementing caching to reduce API calls
2. **Image Loading**: Use Coil for efficient weather icon loading
3. **State Preservation**: Handle configuration changes properly
4. **Memory Leaks**: Use lifecycle-aware components

## Security Best Practices

1. **API Key Management**:
   - Never hardcode API keys in source code
   - Use BuildConfig fields or environment variables
   - Add API key file to .gitignore

2. **Network Security**:
   - Use HTTPS for all API calls
   - Implement certificate pinning for production
   - Validate server responses

## Common Issues and Solutions

### Issue: "Unable to resolve host"
**Solution**: Check internet permission and network connectivity

### Issue: API returns 401 Unauthorized
**Solution**: Verify API key is correct and active

### Issue: App crashes on API call
**Solution**: Ensure all API calls are wrapped in try-catch or Result

### Issue: UI not updating
**Solution**: Check StateFlow collection and recomposition

## Build and Deploy

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

### Running Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

## Code Style

- Follow Kotlin coding conventions
- Use meaningful variable names
- Add comments for complex logic
- Keep functions small and focused
- Use sealed classes for state management
- Prefer composition over inheritance

## Resources

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [OpenWeatherMap API Docs](https://openweathermap.org/api)
- [Material 3 Guidelines](https://m3.material.io/)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)

## Contributing

When contributing:
1. Create a feature branch
2. Write tests for new features
3. Update documentation
4. Follow existing code style
5. Submit a pull request with clear description

## Version History

### v1.0.0 (Current)
- Initial release
- Current weather display
- City search functionality
- Material 3 design
- MVVM architecture

---

Last Updated: October 21, 2025
