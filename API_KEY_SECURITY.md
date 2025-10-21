# API Key Security Guide

## ✅ How Your API Key is Protected

This project uses **best practices** to keep your OpenWeatherMap API key secure and private.

### What We Did

1. **BuildConfig Integration**
   - API key is stored in `local.properties` (NOT in source code)
   - Gradle reads it and injects into `BuildConfig.WEATHER_API_KEY`
   - Code references `BuildConfig.WEATHER_API_KEY` instead of hardcoded values

2. **Git Protection**
   - `local.properties` is listed in `.gitignore`
   - This file will NEVER be committed to git
   - Your API key stays on your local machine only

3. **Template for Collaborators**
   - `local.properties.template` is provided (committed to git)
   - New developers copy and fill in their own API key
   - Each developer has their own key

## 🔒 Setup for Your Machine

### First Time Setup

1. **Get your API key** from [OpenWeatherMap](https://openweathermap.org/api)

2. **Add to local.properties**:
   ```properties
   WEATHER_API_KEY=your_actual_api_key_here
   ```

3. **Sync and build** - Gradle will inject the key into BuildConfig

### Verify It's Working

After syncing, you can verify in your build:
- The generated `BuildConfig.java` (in `app/build/generated/`) will contain your key
- But this file is also gitignored (in the `build/` folder)

## 🚀 For Other Developers

If someone else clones your repository:

1. They **won't** have your `local.properties` file (it's gitignored)
2. They copy `local.properties.template` → `local.properties`
3. They add their own API key
4. They build and run

## ⚠️ What NOT to Do

❌ **Never commit** these files with API keys:
- `local.properties` (already protected)
- Any hardcoded keys in `.kt` files
- Configuration files with secrets

❌ **Never** paste API keys in:
- Code comments
- Commit messages
- Pull request descriptions
- Issue reports

## ✅ What IS Safe to Commit

✅ **These are safe** to commit to GitHub:
- `local.properties.template` (with placeholder values)
- `ApiConfig.kt` (references `BuildConfig.WEATHER_API_KEY`)
- `.gitignore` (lists `local.properties`)
- Build files that read from `local.properties`

## 🔍 Before Pushing to GitHub

### Quick Checklist

```bash
# 1. Check what will be committed
git status

# 2. Verify local.properties is NOT listed
# (should only show tracked files)

# 3. Double-check .gitignore contains:
cat .gitignore | grep local.properties

# 4. Search for any hardcoded keys (should return nothing)
git grep -i "d2c218e715df7e9ae84c6d3571612585"
```

If the grep finds anything, remove it before committing!

## 🛡️ Additional Security Tips

### For Production Apps

1. **Use ProGuard/R8** to obfuscate BuildConfig in release builds
2. **Add key validation** in your app to detect compromised keys
3. **Rotate keys periodically** if you suspect exposure
4. **Use backend proxy** for production (keep keys server-side)

### Example ProGuard Rule

Add to `proguard-rules.pro`:
```proguard
-keepclassmembers class com.min.weatherapp.BuildConfig {
    public static final java.lang.String WEATHER_API_KEY;
}
```

## 🔄 Key Rotation

If you accidentally expose your key:

1. **Immediately** deactivate it on [OpenWeatherMap Dashboard](https://home.openweathermap.org/api_keys)
2. Generate a new API key
3. Update your `local.properties`
4. If committed to git:
   - Remove from git history using `git filter-branch` or BFG Repo-Cleaner
   - Force push (⚠️ warning: rewrites history)

## 📚 Alternative Methods

### Environment Variables (for CI/CD)

For GitHub Actions or CI/CD:

```yaml
# .github/workflows/build.yml
env:
  WEATHER_API_KEY: ${{ secrets.WEATHER_API_KEY }}
```

Then modify `build.gradle.kts`:
```kotlin
buildConfigField("String", "WEATHER_API_KEY", 
    "\"${System.getenv("WEATHER_API_KEY") ?: localProperties.getProperty("WEATHER_API_KEY") ?: ""}\"")
```

### Gradle Properties (Alternative)

You could also use `~/.gradle/gradle.properties`:
```properties
weatherApiKey=your_key_here
```

Then in `build.gradle.kts`:
```kotlin
buildConfigField("String", "WEATHER_API_KEY", 
    "\"${project.findProperty("weatherApiKey") ?: ""}\"")
```

## 📖 Learn More

- [Android Security Best Practices](https://developer.android.com/topic/security/best-practices)
- [Managing API Keys in Android](https://developer.android.com/studio/publish/app-signing)
- [Git Secrets Prevention](https://github.com/awslabs/git-secrets)

---

**Remember:** Your API key is like a password. Keep it secret, keep it safe! 🔐
