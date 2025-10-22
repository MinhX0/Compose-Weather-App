package com.min.weatherapp.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.*

object LocaleHelper {
    fun setLocale(context: Context, languageCode: String) {
        val localeList = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(localeList)
        
        // For older Android versions, also update configuration
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            updateResourcesLegacy(context, languageCode)
        }
    }
    
    private fun updateResourcesLegacy(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        
        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }
    
    fun getStoredLanguage(context: Context): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AppCompatDelegate.getApplicationLocales()[0]?.language
        } else {
            val currentLocale = context.resources.configuration.locales[0].language
            if (currentLocale != "en" && currentLocale != "vi") null else currentLocale
        }
    }

    fun wrapContext(context: Context, languageCode: String): Context {
        val config = context.resources.configuration
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }
}