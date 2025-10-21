package com.min.weatherapp.data.api

import com.min.weatherapp.BuildConfig

/**
 * API Configuration for Weather App
 * 
 * IMPORTANT: Get your free API key from https://openweathermap.org/api
 * Add your API key to local.properties: WEATHER_API_KEY=your_key_here
 */
object ApiConfig {
    val API_KEY: String = BuildConfig.WEATHER_API_KEY
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val UNITS = "metric" // metric = Celsius, imperial = Fahrenheit
    
    // Icon URL format: https://openweathermap.org/img/wn/{icon}@2x.png
    fun getIconUrl(iconCode: String): String {
        return "https://openweathermap.org/img/wn/$iconCode@2x.png"
    }
}
