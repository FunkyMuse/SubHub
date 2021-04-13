package com.crazylegend.subhub.utils

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.recyclerview.getLastVisibleItemPosition
import com.crazylegend.recyclerview.smoothScrollTo
import com.crazylegend.subhub.consts.SCROLL_TO_TOP_VISIBILITY_THRESHOLD
import com.google.android.material.floatingactionbutton.FloatingActionButton


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

var isLoading = false
inline fun RecyclerView.recyclerScrollBackToTop(backToTop: FloatingActionButton?, adapter: RecyclerView.Adapter<*>,
                                                crossinline loadMore: () -> Unit = {}) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            adapter.apply {
                val lastPos = recyclerView.getLastVisibleItemPosition()

                lastPos?.apply {
                    if (this >= adapter.itemCount - 1 && !isLoading) {
                        loadMore()
                    }
                    if (this > SCROLL_TO_TOP_VISIBILITY_THRESHOLD) {
                        backToTop?.visible()
                    } else {
                        backToTop?.gone()
                    }
                }
            }
        }
    })
    backToTop?.setOnClickListener {
        smoothScrollTo(0)
    }
}