package com.crazylegend.subhub.listeners

import com.crazylegend.subhub.pickedDirs.PickedDirModel


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */
interface onDirChosen {
    fun forDirectory(model: PickedDirModel)
}

fun onDirChosenDSL(callback: (pickedDirModel: PickedDirModel) -> Unit = {}) = object : onDirChosen {
    override fun forDirectory(model: PickedDirModel) {
        callback.invoke(model)
    }
}