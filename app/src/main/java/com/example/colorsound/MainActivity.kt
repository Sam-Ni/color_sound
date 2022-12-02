package com.example.colorsound

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.colorsound.ui.ColorSoundApp
import com.example.colorsound.ui.vm.data.ConfigData
import com.example.colorsound.ui.vm.service.PlaySoundService
import com.example.colorsound.util.Injecter
import com.example.colorsound.util.MyContextWrapper
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            ColorSoundApp()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val sharedPreferences = Injecter.get<SharedPreferences>()
        super.attachBaseContext(ContextWrapper(sharedPreferences.getString("locale", "zh")
            ?.let { newBase.setAppLocale(it) }))
    }

    override fun onStop() {
        val configData = Injecter.getMutable<ConfigData>()
        if (!configData.value.backgroundPlay) {
            val playSoundService = Injecter.getService<PlaySoundService>()
            playSoundService.stopAllPlayer()
        }
        super.onStop()
    }
}

fun Context.setAppLocale(language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    return createConfigurationContext(config)
}

