package com.crazylegend.subhub.vms

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crazylegend.kotlinextensions.database.DBResult
import com.crazylegend.subhub.core.AbstractAVM
import com.crazylegend.subhub.dtos.LocalVideoItem
import com.crazylegend.subhub.pickedDirs.PickedDirModel
import com.crazylegend.subhub.pickedDirs.PickedDirRepo


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */

/**
 * Template created by Hristijan to live long and prosper.
 */

class PickedDirVM(application: Application) : AbstractAVM(application) {

    private val pickedDirData: MutableLiveData<DBResult<List<PickedDirModel>>> = MutableLiveData()
    val pickedDirs: LiveData<DBResult<List<PickedDirModel>>> get() = pickedDirData

    private val pickedDirRepo = PickedDirRepo(viewModelScope, component.pickedDirComponent.database?.dao())

    init {
        initDirs()
    }

    private fun initDirs() {
        pickedDirRepo.getAllDirs(pickedDirData)
    }

    fun deleteDir(dirModel: PickedDirModel) = pickedDirRepo.deleteDir(dirModel)

    fun insertDir(dirModel: PickedDirModel) = pickedDirRepo.insertDir(dirModel)

    private val videoListData: MutableLiveData<List<LocalVideoItem>> = MutableLiveData()
    val videoList: MutableLiveData<List<LocalVideoItem>> = videoListData

    fun postVideos(adapterList: MutableList<LocalVideoItem>) {
        videoListData.postValue(adapterList.distinctBy {
            it.md5
        })
    }

    private val videoListDataFiltered: MutableLiveData<List<LocalVideoItem>> = MutableLiveData()
    val videoListFiltered: MutableLiveData<List<LocalVideoItem>> = videoListDataFiltered

    fun filterVideoList(query: String) = videoListDataFiltered.postValue(videoListData.value?.filter {
        it.videoName.toString().contains(query, true)
    })
}