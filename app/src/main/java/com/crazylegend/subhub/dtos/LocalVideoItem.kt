package com.crazylegend.subhub.dtos

import android.net.Uri
import androidx.annotation.Keep

@Keep
data class LocalVideoItem(val photoString: String, val videoName: String?, val videoSize: Long, val md5: String) {
    val photoUri get() = Uri.parse(photoString)
}