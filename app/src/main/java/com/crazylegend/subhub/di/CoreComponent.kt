package com.crazylegend.subhub.di

import android.app.Application
import android.content.SharedPreferences
import com.crazylegend.subhub.utils.SubToast


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
interface CoreComponent {
    val application:Application
    val defaultPrefs : SharedPreferences
    val subToast:SubToast
}