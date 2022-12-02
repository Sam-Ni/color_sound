package com.example.colorsound.ui.vm.service

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.media3.common.Player
import com.example.colorsound.ui.vm.data.ConfigData
import com.example.colorsound.ui.vm.data.ContextData
import com.example.colorsound.ui.vm.data.Language
import com.example.colorsound.util.Injecter
import kotlinx.coroutines.flow.update

class SettingService : ViewModel() {
    private val sharedPreferences = Injecter.get<SharedPreferences>()
    private val configData = Injecter.getMutable<ConfigData>()
    private val playSoundService = Injecter.getService<PlaySoundService>()
    private val contextData = Injecter.getMutable<ContextData>()

    private val nextLanguageMap =
        mapOf(Language.Chinese to Language.English, Language.English to Language.Chinese)

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

    fun changeLanguage() {
        configData.update {
            it.copy(
                language = nextLanguageMap[configData.value.language] ?: Language.Chinese
            )
        }
        val locale = configData.value.language.locate
        sharedPreferences.edit().apply {
            putString(
                "locale", locale
            )
            apply()
        }
//        val packageManager = contextData.value.context.packageManager
//        val intent =
//            packageManager?.getLaunchIntentForPackage(contextData.value.context.packageName)
//        if (intent != null) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            contextData.value.context.startActivity(intent)
//            Log.d("silly", "restart")
//        }
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
