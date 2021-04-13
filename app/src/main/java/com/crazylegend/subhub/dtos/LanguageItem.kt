package com.crazylegend.subhub.dtos

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class LanguageItem(val name: String? = "", val code: String? = "") : Parcelable