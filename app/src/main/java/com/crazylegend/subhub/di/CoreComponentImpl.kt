package com.crazylegend.subhub.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.crazylegend.subhub.utils.SubToast


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class CoreComponentImpl(override val application: Application) : CoreComponent {

    override val defaultPrefs : SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }

    override val subToast by lazy { SubToast(application) }
}