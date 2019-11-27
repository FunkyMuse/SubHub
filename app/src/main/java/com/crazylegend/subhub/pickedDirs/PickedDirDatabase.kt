package com.crazylegend.subhub.pickedDirs

import androidx.room.Database
import androidx.room.RoomDatabase


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */

@Database(entities = [PickedDirModel::class], version = 1, exportSchema = false)
abstract class PickedDirDatabase : RoomDatabase() {
    abstract fun dao(): PickedDirDAO
}