package com.min.weatherapp.util

/**
 * Application-wide constants
 */
object Constants {
    
    // API Configuration
    object Api {
        const val TIMEOUT_SECONDS = 30L
        const val RATE_LIMIT_PER_MINUTE = 60
        const val RATE_LIMIT_PER_MONTH = 1_000_000
    }
    
    // Validation Rules
    object Validation {
        const val MIN_CITY_NAME_LENGTH = 2
        const val MAX_CITY_NAME_LENGTH = 100
        val CITY_NAME_REGEX = Regex("^[a-zA-Z\\s,.-]+$")
    }
    
    // Default Values
    object Defaults {
        const val DEFAULT_CITY = "London"
        const val TEMPERATURE_UNIT = "metric" // metric, imperial, standard
    }
    
    // UI Constants
    object UI {
        const val CARD_ELEVATION_DP = 2
        const val CORNER_RADIUS_DP = 16
        const val DEFAULT_PADDING_DP = 16
        const val ICON_SIZE_LARGE_SP = 100
        const val ICON_SIZE_MEDIUM_SP = 80
        const val ICON_SIZE_SMALL_SP = 48
    }
    
    // Temperature Thresholds (Celsius)
    object Temperature {
        const val FREEZING = 0.0
        const val COOL = 15.0
        const val MODERATE = 25.0
        const val WARM = 35.0
    }
    
    // Humidity Thresholds (%)
    object Humidity {
        const val DRY = 30
        const val COMFORTABLE = 60
        const val HUMID = 80
    }
    
    // Error Messages
    object ErrorMessages {
        const val NO_INTERNET = "No internet connection. Please check your network."
        const val TIMEOUT = "Request timeout. Please try again."
        const val INVALID_API_KEY = "Invalid API key. Please check your configuration."
        const val CITY_NOT_FOUND = "City not found. Please check the spelling."
        const val TOO_MANY_REQUESTS = "Too many requests. Please try again later."
        const val EMPTY_CITY_NAME = "Please enter a city name"
        const val SHORT_CITY_NAME = "City name must be at least 2 characters"
        const val UNKNOWN_ERROR = "An unknown error occurred"
    }
    
    // Popular Cities for Suggestions
    object PopularCities {
        val CITIES = listOf(
            "London", "Tokyo", "Paris", "New York",
            "Sydney", "Dubai", "Singapore", "Berlin",
            "Rome", "Amsterdam", "Barcelona", "Seoul"
        )
    }
}
