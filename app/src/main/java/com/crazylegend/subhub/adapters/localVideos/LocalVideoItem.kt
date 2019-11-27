package com.crazylegend.subhub.adapters.localVideos

import android.net.Uri

data class LocalVideoItem(val photoUri: Uri, val videoName: String?, val videoType: String?, val videoSize: Long)