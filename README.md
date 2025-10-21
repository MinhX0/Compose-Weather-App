# Weather App

A simple and elegant Android weather application built with Jetpack Compose and Kotlin.

## Features

- 🌤️ Display current weather information for any city
- 🌡️ Shows temperature, feels-like temperature, min/max temperatures
- 💨 Wind speed and direction
- 💧 Humidity and atmospheric pressure
- ☁️ Cloudiness and visibility
- 🌅 Sunrise and sunset times
- 🎨 Beautiful Material 3 design with gradient backgrounds
- 🔍 Search functionality to find weather for any city

## Screenshots

The app displays:
- City name and country
- Current date
- Weather icon and description
- Current temperature
- Detailed weather information (humidity, pressure, wind speed, etc.)
- Sunrise and sunset times

## Technologies Used

- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Material 3** - Design system
- **Retrofit** - REST API client
- **Coroutines & Flow** - Asynchronous programming
- **ViewModel** - MVVM architecture
- **OkHttp** - HTTP client
- **Gson** - JSON parsing
- **OpenWeatherMap API** - Weather data provider

## Architecture

The app follows the MVVM (Model-View-ViewModel) architecture pattern:

```
app/
├── data/
│   ├── api/
│   │   ├── WeatherApiService.kt      # API interface
│   │   └── RetrofitInstance.kt        # Retrofit configuration
│   ├── model/
│   │   └── WeatherData.kt             # Data models
│   └── repository/
│       └── WeatherRepository.kt       # Data repository
├── ui/
│   ├── screens/
│   │   └── WeatherScreen.kt           # Main weather screen UI
│   └── theme/                         # App theme
└── viewmodel/
    └── WeatherViewModel.kt            # ViewModel with UI state
```

## Setup Instructions

### Prerequisites

- Android Studio (latest version recommended)
- Android SDK (API level 24 or higher)
- OpenWeatherMap API key

### Installation

1. Clone this repository:
   ```bash
   git clone <repository-url>
   ```

2. Open the project in Android Studio

3. Get a free API key from [OpenWeatherMap](https://openweathermap.org/api):
   - Sign up for a free account
   - Generate an API key
   - Copy your API key

4. Add your API key to the project:
   - Open `app/src/main/java/com/min/weatherapp/data/repository/WeatherRepository.kt`
   - Replace `YOUR_API_KEY_HERE` with your actual API key:
     ```kotlin
     private val apiKey = "your_actual_api_key_here"
     ```

5. Sync the project with Gradle files

6. Run the app on an emulator or physical device

## Usage

1. **Launch the app** - The app will load with a default city (London)
2. **Search for a city** - Enter a city name in the search bar and tap the search icon
3. **View weather details** - Scroll through the screen to see all weather information

## API Information

This app uses the [OpenWeatherMap API](https://openweathermap.org/api) to fetch current weather data.

**API Endpoint Used:**
- Current Weather Data: `https://api.openweathermap.org/data/2.5/weather`

**Parameters:**
- `q` - City name
- `appid` - Your API key
- `units` - Temperature units (metric for Celsius)

## Dependencies

All dependencies are managed through Gradle Version Catalogs (`libs.versions.toml`):

- Retrofit 2.9.0
- OkHttp 4.12.0
- Gson 2.10.1
- Jetpack Compose (latest BOM)
- Material 3
- Lifecycle ViewModel Compose
- Coil (for image loading)

## Future Enhancements

Potential features to add:
- 📍 Location-based weather (GPS)
- 📅 5-day weather forecast
- 🌙 Dark mode theme
- 💾 Save favorite cities
- 🔔 Weather notifications
- 🗺️ Weather maps
- 📊 Weather charts and graphs

## Permissions

The app requires the following permissions:
- `INTERNET` - To fetch weather data from the API
- `ACCESS_NETWORK_STATE` - To check network connectivity

## License

This project is open source and available for educational purposes.

## Credits

- Weather data provided by [OpenWeatherMap](https://openweathermap.org/)
- Icons and emojis used for weather conditions
- Built with ❤️ using Jetpack Compose

## Troubleshooting

### Common Issues

1. **No data showing:**
   - Make sure you've added your API key
   - Check your internet connection
   - Verify the API key is valid

2. **Build errors:**
   - Sync project with Gradle files
   - Clean and rebuild the project
   - Update Android Studio to the latest version

3. **API errors:**
   - Free tier has rate limits (60 calls/minute)
   - Check city name spelling
   - Verify API key is active

## Contact

For questions or suggestions, please open an issue in the repository.

---

**Note:** Remember to never commit your API key to version control. Consider using BuildConfig fields or environment variables for production apps.
