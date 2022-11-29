package com.example.colorsound.ui.vm.service

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.colorsound.ui.vm.data.ConfigData
import com.example.colorsound.util.ConfigName
import com.example.colorsound.util.Injecter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SettingService: ViewModel() {
    private val sharedPreferences: SharedPreferences = Injecter.get("SharedPreferences")
    private val config: MutableStateFlow<ConfigData> = Injecter.get("ConfigData")

    fun onIsRepeatPlayChanged(value: Boolean) {
        config.update { it.copy(isRepeatPlay = value) } //先更新config，驱动UI更新
        sharedPreferences.edit().apply {        //再把config存到sharedPreferences中
            putBoolean(
                ConfigName.isRepeatPlay, config.value.isRepeatPlay
            )
            apply()
        }
    }
}
