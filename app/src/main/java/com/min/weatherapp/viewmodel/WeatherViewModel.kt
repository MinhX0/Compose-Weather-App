package com.min.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.min.weatherapp.data.model.WeatherResponse
import com.min.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WeatherUiState {
    object Idle : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val weather: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    
    init {
        // Load default city weather on start
        getWeatherByCity("London")
    }
    
    fun getWeatherByCity(city: String) {
        if (city.isBlank()) {
            _uiState.value = WeatherUiState.Error("Please enter a city name")
            return
        }
        
        if (city.length < 2) {
            _uiState.value = WeatherUiState.Error("City name must be at least 2 characters")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            
            val result = repository.getCurrentWeather(city.trim())
            
            _uiState.value = if (result.isSuccess) {
                WeatherUiState.Success(result.getOrThrow())
            } else {
                WeatherUiState.Error(
                    result.exceptionOrNull()?.message ?: "Unknown error occurred"
                )
            }
        }
    }
    
    fun getWeatherByCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            
            val result = repository.getCurrentWeatherByCoordinates(latitude, longitude)
            
            _uiState.value = if (result.isSuccess) {
                WeatherUiState.Success(result.getOrThrow())
            } else {
                WeatherUiState.Error(
                    result.exceptionOrNull()?.message ?: "Unknown error occurred"
                )
            }
        }
    }
}
