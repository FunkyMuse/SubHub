package com.crazylegend.subhub.vms

import android.app.Application
import android.content.ContentUris
import android.provider.MediaStore
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crazylegend.kotlinextensions.cursor.getSafeColumn
import com.crazylegend.kotlinextensions.safeOffer
import com.crazylegend.subhub.core.AbstractAVM
import com.crazylegend.subhub.dtos.LocalVideoItem
import com.crazylegend.subhub.utils.registerObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Created by crazy on 5/8/20 to long live and prosper !
 */
internal class VideosVM(application: Application) : AbstractAVM(application) {

    private val videoData = MutableLiveData<List<LocalVideoItem>>()
    val videos: LiveData<List<LocalVideoItem>> = videoData

    val filteredVideos = MutableLiveData<List<LocalVideoItem>>()


    fun loadVideos() {
        if (canLoad) {
            viewModelScope.launch {
                videoData.postValue(queryVideos())
                initializeContentObserver()
            }
        }
    }

    private fun initializeContentObserver() {
        if (contentObserver == null) {
            contentObserver = contentResolver.registerObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                canLoad = true
                loadVideos()
            }
        }
    }

    private suspend fun queryVideos(): List<LocalVideoItem> {
        loadingIndicatorData.safeOffer(true)
        val video = mutableListOf<LocalVideoItem>()

        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"
        withContext(Dispatchers.IO) {
            val projection =
                    arrayOf(
                            MediaStore.Video.Media._ID,
                            MediaStore.Video.Media.DISPLAY_NAME,
                            MediaStore.Video.Media.SIZE
                    )

            contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder)?.use { cursor ->

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getSafeColumn(MediaStore.Video.Media.SIZE)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getStringOrNull(displayNameColumn)
                    val size = sizeColumn?.let { cursor.getIntOrNull(it) }

                    val contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                    val videoModel = LocalVideoItem(id, displayName, contentUri, size)
                    video += videoModel
                }
            }
        }
        canLoad = false
        loadingIndicatorData.safeOffer(false)
        return video
    }

    fun filterVideos(query: String) {
        val filterList = videoData.value
        filteredVideos.value = if (filterList.isNullOrEmpty()) {
            videoData.value
        } else {
            filterList.filter { it.displayName?.contains(query, true) ?: false }
        }
    }
}
