package com.example.colorsound.ui.vm.service

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.media3.common.Player
import com.example.colorsound.ui.vm.data.ConfigData
import com.example.colorsound.ui.vm.data.Language
import com.example.colorsound.util.ConfigName
import com.example.colorsound.util.Injecter
import kotlinx.coroutines.flow.update

class SettingService : ViewModel() {
    private val sharedPreferences = Injecter.get<SharedPreferences>()
    private val configData = Injecter.getMutable<ConfigData>()
    private val playSoundService = Injecter.getService<PlaySoundService>()

    fun onIsRepeatPlayChanged(value: Boolean) {
        configData.update { it.copy(isRepeatPlay = value) } //先更新config，驱动UI更新
        sharedPreferences.edit().apply {        //再把config存到sharedPreferences中
            putBoolean(
                "IsRepeatPlay", configData.value.isRepeatPlay
            )
            apply()
        }
        when (value) {
            true -> playSoundService.setRepeatMode(Player.REPEAT_MODE_ALL)
            false -> playSoundService.setRepeatMode(Player.REPEAT_MODE_OFF)
        }
    }

    fun onLanguageSelect(language: Language) {
        val locale = when (language) {
            Language.Chinese -> "zh"
            Language.English -> "en"
        }
        configData.update { it.copy(language = language) }
        sharedPreferences.edit().apply {
            putString(
                "locale", locale
            )
            apply()
        }
    }

    fun onIsBackPlayChanged(value: Boolean) {
        configData.update { it.copy(backgroundPlay = value) }
        sharedPreferences.edit().apply {        //再把config存到sharedPreferences中
            putBoolean(
                "BackgroundPlay", value
            )
            apply()
        }
    }
}
