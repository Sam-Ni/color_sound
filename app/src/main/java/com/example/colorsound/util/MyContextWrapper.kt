package com.example.colorsound.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import java.util.*

class MyContextWrapper(base: Context?) : ContextWrapper(base) {
    companion object Ctx {
        fun wrap(context: Context, language: String): ContextWrapper {
            val config = context.resources.configuration
            val sysLocale = getSystemLocale(config)
            if (language != "" && sysLocale.language != language) {
                val locale = Locale(language)
                Locale.setDefault(locale)
                setSystemLocale(config, locale)
            }
            return MyContextWrapper(context.createConfigurationContext(config))
        }
        private fun getSystemLocale(config: Configuration): Locale {
            return config.locales.get(0)
        }
        private fun setSystemLocale(config: Configuration, locale: Locale) {
            config.setLocale(locale)
        }
    }
}