package com.min.weatherapp.util

import java.text.SimpleDateFormat
import java.util.*

object WeatherUtils {
    
    /**
     * Format Unix timestamp to readable date
     * Example: "Monday, October 21, 2025"
     */
    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }
    
    /**
     * Format Unix timestamp to time
     * Example: "14:30"
     */
    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }
    
    /**
     * Get emoji for weather condition
     */
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
            "dust", "sand" -> "🌪️"
            "ash" -> "🌋"
            "squall" -> "💨"
            "tornado" -> "🌪️"
            else -> "🌤️"
        }
    }
    
    /**
     * Format wind direction from degrees
     */
    fun getWindDirection(degrees: Int): String {
        return when (degrees) {
            in 0..22 -> "N"
            in 23..67 -> "NE"
            in 68..112 -> "E"
            in 113..157 -> "SE"
            in 158..202 -> "S"
            in 203..247 -> "SW"
            in 248..292 -> "W"
            in 293..337 -> "NW"
            in 338..360 -> "N"
            else -> "N/A"
        }
    }
    
    /**
     * Get humidity level description
     */
    fun getHumidityDescription(humidity: Int): String {
        return when {
            humidity < 30 -> "Dry"
            humidity < 60 -> "Comfortable"
            humidity < 80 -> "Humid"
            else -> "Very Humid"
        }
    }
    
    /**
     * Get UV index description (placeholder for future implementation)
     */
    fun getUVDescription(uvIndex: Int): String {
        return when {
            uvIndex <= 2 -> "Low"
            uvIndex <= 5 -> "Moderate"
            uvIndex <= 7 -> "High"
            uvIndex <= 10 -> "Very High"
            else -> "Extreme"
        }
    }
    
    /**
     * Validate city name
     */
    fun isValidCityName(city: String): Boolean {
        return city.isNotBlank() && 
               city.length >= 2 && 
               city.length <= 100 &&
               city.matches(Regex("^[a-zA-Z\\s,.-]+$"))
    }
    
    /**
     * Get temperature color suggestion based on temperature
     */
    fun getTemperatureColor(temp: Double): String {
        return when {
            temp < 0 -> "cold" // Blue tones
            temp < 15 -> "cool" // Light blue
            temp < 25 -> "moderate" // Green/Yellow
            temp < 35 -> "warm" // Orange
            else -> "hot" // Red
        }
    }
}

/**
 * Extension function for String to capitalize first letter of each word
 */
fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

/**
 * Extension function for Double to format temperature
 */
fun Double.toTemperatureString(): String {
    return "${this.toInt()}°C"
}
