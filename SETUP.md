# Quick Setup Guide

## 🚀 Getting Started in 5 Minutes

### Step 1: Get Your API Key

1. Go to [OpenWeatherMap](https://openweathermap.org/api)
2. Click "Sign Up" (it's free!)
3. Verify your email
4. Go to your profile → "My API Keys"
5. Copy your API key

### Step 2: Add API Key to the App

1. Open the project in Android Studio
2. Look for the `local.properties` file in the project root
   - If it doesn't exist, copy `local.properties.template` and rename to `local.properties`
3. Open `local.properties`
4. Add this line (replace with your actual key):
   ```properties
   WEATHER_API_KEY=abc123def456ghi789
   ```
5. Save the file
6. **Important:** This file is automatically excluded from git (in `.gitignore`), so your API key stays private!

### Step 3: Sync and Build

1. Click **File → Sync Project with Gradle Files**
2. Wait for the sync to complete
3. Click the **Run** button (green play icon) or press `Shift + F10`

### Step 4: Choose a Device

- **Emulator**: Select an Android emulator from the device dropdown
- **Physical Device**: Connect your Android phone via USB (make sure USB debugging is enabled)

### Step 5: Run the App! 🎉

The app will install and launch on your device. You should see:
- A search bar at the top
- Weather information for London (default city)
- Temperature, humidity, wind speed, and more!

## 🔍 Using the App

1. **Search for a city**: Type a city name in the search bar and tap the search icon
2. **View weather details**: Scroll down to see all weather information
3. **Try different cities**: Search for "Tokyo", "Paris", "New York", etc.

## ⚠️ Troubleshooting

### Problem: App shows "Error" or no data

**Solutions:**
- Check your internet connection
- Verify your API key is correct
- Make sure you saved the file after adding the API key
- Rebuild the project: **Build → Rebuild Project**

### Problem: Build errors

**Solutions:**
- Click **File → Invalidate Caches → Invalidate and Restart**
- Make sure you have the latest version of Android Studio
- Check that Gradle sync completed successfully

### Problem: API key not working

**Solutions:**
- New API keys can take up to 2 hours to activate
- Check your OpenWeatherMap account to ensure the key is active
- Make sure you copied the entire key without extra spaces

### Problem: Slow API response

**Note:** The free tier of OpenWeatherMap API may have rate limits:
- 60 calls per minute
- 1,000,000 calls per month

## 📱 Minimum Requirements

- **Android Version**: Android 7.0 (API 24) or higher
- **Internet**: Required for fetching weather data
- **Storage**: ~20 MB for the app

## 🎨 What You'll See

The app displays:
- 🏙️ City name and country
- 📅 Current date
- ☀️ Weather condition with emoji
- 🌡️ Temperature in Celsius
- 💨 Wind speed
- 💧 Humidity percentage
- 🌅 Sunrise time
- 🌇 Sunset time
- And more weather details!

## 🔄 Next Steps

Once you have the app running:
1. ✅ Test it with different cities
2. ✅ Check out the code structure
3. ✅ Read the [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md) for detailed documentation
4. ✅ Explore potential features in [TODO.md](TODO.md)
5. ✅ Start customizing and adding your own features!

## 💡 Tips

- **Search tip**: Use city names in English for best results
- **Format**: You can search "London,UK" or "New York,US" for specific countries
- **Temperature**: The app uses Celsius by default (metric units)

## 📞 Need Help?

- Check the [README.md](README.md) for more detailed information
- Review the [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md) for technical details
- Look at the code comments for inline documentation

---

**Enjoy building your weather app!** ☀️🌧️⛈️❄️

Built with ❤️ using Jetpack Compose and Kotlin
