package com.example.core_ui.dynamicscreen.component

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class LanguageChangeHelper {

    fun changeLanguage(context: Context, languageCode: String) {

        //if needed we can set in pref.

        //version >= 13
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(languageCode)
        } else {
            //version < 13
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
        }
    }

    fun getLanguageCode(context: Context): String {
        // 1) Prefer app-specific language if set
        val appLanguage: String? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.getSystemService(LocaleManager::class.java)
                    .applicationLocales[0]
                    ?.language
            } else {
                AppCompatDelegate.getApplicationLocales()[0]
                    ?.language
            }

        if (!appLanguage.isNullOrBlank()) return appLanguage

        // 2) Otherwise fall back to device/system language (first install case)
        val systemLanguage: String? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.resources.configuration.locales[0]?.language
            } else {
                @Suppress("DEPRECATION")
                context.resources.configuration.locale?.language
            }

        return systemLanguage?.takeIf { it.isNotBlank() } ?: "en"
    }


//    fun getLanguageCode(context: Context,): String {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            context.getSystemService(LocaleManager::class.java).applicationLocales[0]?.
//            toLanguageTag()?.split("-")?.first() ?: "en"
//        } else {
//            //version < 13
//            AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag()?.
//            split("-")?.first() ?: "en"
//        }
//    }
}