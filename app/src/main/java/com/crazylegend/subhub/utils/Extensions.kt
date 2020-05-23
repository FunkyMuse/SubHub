package com.crazylegend.subhub.utils

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */


fun String?.isNullStringOrEmpty(): Boolean {
    return this.isNullOrEmpty() || this == "null"
}


fun String?.isNotNullStringOrEmpty(): Boolean {
    return !isNullStringOrEmpty()
}

fun ContentResolver.registerObserver(
        uri: Uri,
        observer: (change: Boolean) -> Unit
): ContentObserver {
    val contentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            observer(selfChange)
        }
    }
    registerContentObserver(uri, true, contentObserver)
    return contentObserver
}