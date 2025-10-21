package com.min.weatherapp.data.api

/**
 * API Configuration for Weather App
 * 
 * IMPORTANT: Get your free API key from https://openweathermap.org/api
 * Replace YOUR_API_KEY_HERE with your actual API key
 */
object ApiConfig {
    const val API_KEY = "YOUR_API_KEY_HERE"
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val UNITS = "metric" // metric = Celsius, imperial = Fahrenheit
    
    // Icon URL format: https://openweathermap.org/img/wn/{icon}@2x.png
    fun getIconUrl(iconCode: String): String {
        return "https://openweathermap.org/img/wn/$iconCode@2x.png"
    }
}
