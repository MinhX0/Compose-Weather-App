package com.min.weatherapp

/**
 * Application information and metadata
 */
object AppInfo {
    const val APP_NAME = "Weather App"
    const val VERSION_NAME = "1.0.0"
    const val VERSION_CODE = 1
    
    const val DESCRIPTION = "A simple and elegant weather application built with Jetpack Compose"
    
    const val DEVELOPER = "Your Name"
    const val GITHUB_URL = "https://github.com/yourusername/weather-app"
    
    const val DATA_SOURCE = "OpenWeatherMap"
    const val DATA_SOURCE_URL = "https://openweathermap.org"
    
    const val BUILD_DATE = "October 21, 2025"
    
    object Features {
        const val CURRENT_WEATHER = "Current Weather Information"
        const val SEARCH = "Search by City Name"
        const val DETAILED_INFO = "Detailed Weather Data"
        const val BEAUTIFUL_UI = "Modern Material Design 3"
    }
    
    object Tech {
        const val KOTLIN = "Kotlin"
        const val JETPACK_COMPOSE = "Jetpack Compose"
        const val RETROFIT = "Retrofit"
        const val MATERIAL3 = "Material 3"
        const val COROUTINES = "Kotlin Coroutines"
        const val MVVM = "MVVM Architecture"
    }
}
