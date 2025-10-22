package com.min.weatherapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.min.weatherapp.ui.screens.WeatherScreen
import com.min.weatherapp.ui.theme.WeatherAppTheme
import com.min.weatherapp.viewmodel.WeatherViewModel
import com.min.weatherapp.util.LocaleHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply saved language
        LocaleHelper.getStoredLanguage(this)?.let { languageCode ->
            LocaleHelper.setLocale(this, languageCode)
        }

        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                val viewModel: WeatherViewModel = viewModel()
                WeatherScreen(viewModel = viewModel)
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val storedLanguage = LocaleHelper.getStoredLanguage(newBase)
        super.attachBaseContext(
            if (storedLanguage != null) {
                LocaleHelper.wrapContext(newBase, storedLanguage)
            } else {
                newBase
            }
        )
    }
}