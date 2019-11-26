package com.crazylegend.subhub.adapters.chooseLanguage

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LanguageItem(val name: String, val code: String) : Parcelable