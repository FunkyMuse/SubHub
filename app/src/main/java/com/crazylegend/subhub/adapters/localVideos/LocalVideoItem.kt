package com.crazylegend.subhub.adapters.localVideos

import android.net.Uri

data class LocalVideoItem(val photoString: String, val videoName: String?, val videoSize: Long, val md5: String) {
    val photoUri get() = Uri.parse(photoString)
}