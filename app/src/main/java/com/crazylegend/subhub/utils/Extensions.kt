package com.crazylegend.subhub.utils

import android.content.Context
import android.content.Intent
import androidx.documentfile.provider.DocumentFile
import com.crazylegend.subhub.pickedDirs.PickedDirModel


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */

fun Intent?.toPickedDirModel(context: Context): PickedDirModel? {
    val uri = this?.data ?: return null
    val pickedDir = DocumentFile.fromTreeUri(context, uri)
    pickedDir ?: return null
    return PickedDirModel(pickedDir.name.toString(), pickedDir.uri.toString())
}
