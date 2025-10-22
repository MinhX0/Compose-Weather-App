# WeatherApp

A simple Android weather app built with Kotlin and Jetpack Compose. It shows current conditions for any city using the OpenWeatherMap API.

## Quick Start

Requirements:
- Android Studio (latest stable)
- Android SDK 24+
- An OpenWeatherMap API key

1) Clone the repo
```bat
git clone <repo-url>
cd WeatherApp
```

2) Add your API key (kept out of git)
```bat
copy local.properties.template local.properties
```
Open `local.properties` and set:
```properties
WEATHER_API_KEY=your_actual_api_key_here
```

3) Build and run (Windows cmd)
```bat
gradlew.bat assembleDebug
gradlew.bat installDebug
```
Or open in Android Studio and press Run.

## Usage
- Launch the app and use the search bar to find a city.
- View temperature, humidity, wind, pressure, clouds, and sunrise/sunset.

## Troubleshooting
- No data loading: ensure your API key is set and your device/emulator has internet.
- 401/404 from API: check city spelling and that your key is active (new keys can take up to ~2 hours).

## License
No license file is included. Unless a license is added, all rights are reserved by the repository owner.
