package com.min.weatherapp.data.repository

import com.min.weatherapp.data.api.ApiConfig
import com.min.weatherapp.data.api.RetrofitInstance
import com.min.weatherapp.data.model.WeatherResponse

class WeatherRepository {
    private val apiService = RetrofitInstance.weatherApiService
    private val apiKey = ApiConfig.API_KEY
    
    suspend fun getCurrentWeather(city: String): Result<WeatherResponse> {
        return try {
            if (city.isBlank()) {
                return Result.failure(IllegalArgumentException("City name cannot be empty"))
            }
            val response = apiService.getCurrentWeather(city, apiKey, ApiConfig.UNITS)
            Result.success(response)
        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Invalid API key. Please check your configuration."))
                404 -> Result.failure(Exception("City not found. Please check the spelling."))
                429 -> Result.failure(Exception("Too many requests. Please try again later."))
                else -> Result.failure(Exception("Server error: ${e.message()}"))
            }
        } catch (e: java.net.UnknownHostException) {
            Result.failure(Exception("No internet connection. Please check your network."))
        } catch (e: java.net.SocketTimeoutException) {
            Result.failure(Exception("Request timeout. Please try again."))
        } catch (e: Exception) {
            Result.failure(Exception("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }
    
    suspend fun getCurrentWeatherByCoordinates(
        latitude: Double,
        longitude: Double
    ): Result<WeatherResponse> {
        return try {
            val response = apiService.getCurrentWeatherByCoordinates(
                latitude, 
                longitude, 
                apiKey,
                ApiConfig.UNITS
            )
            Result.success(response)
        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Invalid API key. Please check your configuration."))
                404 -> Result.failure(Exception("Location not found."))
                429 -> Result.failure(Exception("Too many requests. Please try again later."))
                else -> Result.failure(Exception("Server error: ${e.message()}"))
            }
        } catch (e: java.net.UnknownHostException) {
            Result.failure(Exception("No internet connection. Please check your network."))
        } catch (e: java.net.SocketTimeoutException) {
            Result.failure(Exception("Request timeout. Please try again."))
        } catch (e: Exception) {
            Result.failure(Exception("An error occurred: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }
}
