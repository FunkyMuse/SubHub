package com.crazylegend.subhub.pickedDirs

import android.content.Context
import androidx.room.Room
import com.crazylegend.subhub.consts.PICKED_DIR_DB_NAME


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */
object PickedDirDB {

    private var instance: PickedDirDatabase? = null

    fun purgeDBInstance() {
        instance = null
    }

    fun getInstance(context: Context): PickedDirDatabase? {
        if (instance == null) {
            instance = Room.databaseBuilder(context, PickedDirDatabase::class.java, PICKED_DIR_DB_NAME)
                    .build()
        }
        return instance
    }
}