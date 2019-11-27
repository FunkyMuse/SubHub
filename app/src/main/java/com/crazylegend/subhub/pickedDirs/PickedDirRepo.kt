package com.crazylegend.subhub.pickedDirs

import androidx.lifecycle.MutableLiveData
import com.crazylegend.kotlinextensions.coroutines.makeDBCall
import com.crazylegend.kotlinextensions.coroutines.makeDBCallListFlow
import com.crazylegend.kotlinextensions.database.DBResult
import kotlinx.coroutines.CoroutineScope


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */
class PickedDirRepo(private val viewModelScope: CoroutineScope, private val dao: PickedDirDAO?) {
    fun getAllDirs(pickedDirData: MutableLiveData<DBResult<List<PickedDirModel>>>) {
        viewModelScope.makeDBCallListFlow(pickedDirData, true) {
            dao?.getAllDirs()
        }
    }

    fun insertDir(dirModel: PickedDirModel) {
        viewModelScope.makeDBCall {
            dao?.insertDir(dirModel)
        }
    }

    fun deleteDir(dirModel: PickedDirModel) {
        viewModelScope.makeDBCall {
            dao?.deleteDir(dirModel)
        }
    }
}