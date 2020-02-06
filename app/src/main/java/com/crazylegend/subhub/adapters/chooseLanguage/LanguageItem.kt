package com.crazylegend.subhub.adapters.chooseLanguage

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class LanguageItem(val name: String?, val code: String?) : Parcelable