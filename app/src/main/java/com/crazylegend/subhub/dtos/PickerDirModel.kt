package com.crazylegend.subhub.dtos

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.documentfile.provider.DocumentFile
import kotlinx.android.parcel.Parcelize


/**
 * Created by crazy on 5/23/20 to long live and prosper !
 */
@Parcelize
@Keep
data class PickedDirModel(
        val dir: Uri
) : Parcelable {

    fun getName(context: Context) = dir.let { DocumentFile.fromTreeUri(context, it)?.name }
}