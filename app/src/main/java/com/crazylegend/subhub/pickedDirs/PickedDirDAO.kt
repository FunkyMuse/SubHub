package com.crazylegend.subhub.pickedDirs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */

@Dao
interface PickedDirDAO {

    @Query("select * from pickedDirs")
    fun getAllDirs(): Flow<List<PickedDirModel>>

    @Insert(onConflict = REPLACE)
    suspend fun insertDir(dirModel: PickedDirModel)

    @Delete
    suspend fun deleteDir(dirModel: PickedDirModel)

}