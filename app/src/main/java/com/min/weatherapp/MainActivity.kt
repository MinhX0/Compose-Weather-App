package com.min.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.min.weatherapp.ui.screens.WeatherScreen
import com.min.weatherapp.ui.theme.WeatherAppTheme
import com.min.weatherapp.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                val viewModel: WeatherViewModel = viewModel()
                WeatherScreen(viewModel = viewModel)
            }
        }
    }
}