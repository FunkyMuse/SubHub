package com.crazylegend.subhub.utils

import android.content.Context
import com.crazylegend.kotlinextensions.sharedprefs.putBoolean


/**
 * Created by crazy on 11/28/19 to long live and prosper !
 */


private const val ONE_TIME_FOLDER_SOURCES_PREFS_NAME = "folderSourcePrefs"
private val Context.oneTimePrefsFolderSources get() = getSharedPreferences(ONE_TIME_FOLDER_SOURCES_PREFS_NAME, Context.MODE_PRIVATE)

private const val SNACK_SHOWN_PREF = "isSnackShown"
val Context.isSnackbarFolderSourcesShown get() = oneTimePrefsFolderSources.getBoolean(SNACK_SHOWN_PREF, false)

fun Context.setSnackShown() {
    oneTimePrefsFolderSources.putBoolean(SNACK_SHOWN_PREF, true)
}