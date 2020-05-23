package com.crazylegend.subhub.utils

import android.content.Context
import com.crazylegend.kotlinextensions.sharedprefs.getObject
import com.crazylegend.kotlinextensions.sharedprefs.putObject
import com.crazylegend.subhub.dtos.LanguageItem


/**
 * Created by crazy on 5/23/20 to long live and prosper !
 */

private const val LANGUAGE_PREFS_NAME = "languagePrefs"
private val Context.languagePrefs get() = getSharedPreferences(LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE)

private const val SELECTED_LANGUAGE = "selectedLANGUAGE"
val Context.getSelectedLanguage get() = languagePrefs.getObject<LanguageItem>(SELECTED_LANGUAGE)

fun Context.removeSelectedLanguage() {
    languagePrefs.putObject(SELECTED_LANGUAGE, null)
}

fun Context.addSelectedLanguage(languageItem: LanguageItem) {
    languagePrefs.putObject(SELECTED_LANGUAGE, languageItem)
}