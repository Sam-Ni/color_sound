package com.example.colorsound.ui.vm.service

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colorsound.ColorSoundApplication
import com.example.colorsound.ui.vm.data.ConfigData
import com.example.colorsound.util.ConfigName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SettingService(
    private val sharedPreferences: SharedPreferences,
    private val config: MutableStateFlow<ConfigData>
) : ViewModel() {

    fun onIsRepeatPlayChanged(value: Boolean) {
        config.update { it.copy(isRepeatPlay = value) } //先更新config，驱动UI更新
        sharedPreferences.edit().apply {        //再把config存到sharedPreferences中
            putBoolean(
                ConfigName.isRepeatPlay, config.value.isRepeatPlay
            )
            apply()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ColorSoundApplication)

                val sharedPreferences = application.container.sharedPreferences
                val config = application.container.configData

                SettingService(sharedPreferences, config)
            }
        }
    }
}
