package com.min.weatherapp.data.model

data class SavedLocation(
    val city: String,
    val country: String,
    val isFavorite: Boolean = false
)