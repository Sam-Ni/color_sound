package com.example.colorsound.ui.vm.service

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.colorsound.ui.vm.data.ConfigData
import com.example.colorsound.util.ConfigName
import com.example.colorsound.util.Injecter
import kotlinx.coroutines.flow.update

class SettingService : ViewModel() {
    private val sharedPreferences = Injecter.get<SharedPreferences>()
    private val configData = Injecter.getMutable<ConfigData>()

    fun onIsRepeatPlayChanged(value: Boolean) {
        configData.update { it.copy(isRepeatPlay = value) } //先更新config，驱动UI更新
        sharedPreferences.edit().apply {        //再把config存到sharedPreferences中
            putBoolean(
                ConfigName.isRepeatPlay, configData.value.isRepeatPlay
            )
            apply()
        }
    }
}
