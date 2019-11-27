package com.crazylegend.subhub.di.pickedDirDB

import android.app.Application
import com.crazylegend.subhub.pickedDirs.PickedDirDB


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */
class PickedDirComponentImpl(private val application: Application) : PickedDirComponent {

    override val database by lazy {
        PickedDirDB.getInstance(application)
    }


}