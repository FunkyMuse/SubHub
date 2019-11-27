package com.crazylegend.subhub.pickedDirs

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import androidx.documentfile.provider.DocumentFile
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crazylegend.kotlinextensions.randomUUIDstring
import kotlinx.android.parcel.Parcelize


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */
@Parcelize
@Entity(tableName = "pickedDirs")
data class PickedDirModel(

        @ColumnInfo(name = "dirName")
        val name: String,
        @ColumnInfo(name = "destination")
        val destinationString: String,
        @PrimaryKey
        @ColumnInfo(name = "key")
        val key: String = randomUUIDstring
) : Parcelable {
    val dir get() = Uri.parse(destinationString)

    fun pickedDir(context: Context) = DocumentFile.fromTreeUri(context, dir)
}