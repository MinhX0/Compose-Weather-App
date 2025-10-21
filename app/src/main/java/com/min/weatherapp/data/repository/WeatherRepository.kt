package com.min.weatherapp.data.repository

import com.min.weatherapp.data.api.RetrofitInstance
import com.min.weatherapp.data.model.WeatherResponse

class WeatherRepository {
    private val apiService = RetrofitInstance.weatherApiService
    
    // Note: Replace with your actual OpenWeatherMap API key
    // Get a free API key from: https://openweathermap.org/api
    private val apiKey = "YOUR_API_KEY_HERE"
    
    suspend fun getCurrentWeather(city: String): Result<WeatherResponse> {
        return try {
            val response = apiService.getCurrentWeather(city, apiKey)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getCurrentWeatherByCoordinates(
        latitude: Double,
        longitude: Double
    ): Result<WeatherResponse> {
        return try {
            val response = apiService.getCurrentWeatherByCoordinates(latitude, longitude, apiKey)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
