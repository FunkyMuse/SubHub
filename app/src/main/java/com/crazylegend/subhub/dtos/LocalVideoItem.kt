package com.crazylegend.subhub.dtos

import android.net.Uri
import androidx.annotation.Keep
import com.crazylegend.kotlinextensions.tryOrElse

@Keep
data class LocalVideoItem(
        val id: Long,
        val displayName: String?,
        val contentUri: Uri?,
        val size: Int?
) {
    val videoSize
        get() = tryOrElse(0L) {
            size?.toLong() ?: 0L
        }
}