package com.crazylegend.subhub.pickedDirs

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import androidx.documentfile.provider.DocumentFile
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crazylegend.subhub.utils.countSafVideoFiles
import kotlinx.android.parcel.Parcelize

/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */
@Parcelize
@Entity(tableName = "pickedDirs")
data class PickedDirModel(
        @ColumnInfo(name = "dirName")
        val name: String,
        @PrimaryKey
        @ColumnInfo(name = "destination")
        val destinationString: String
) : Parcelable {
    val dir get() = Uri.parse(destinationString)
    fun pickedDir(context: Context) = DocumentFile.fromTreeUri(context, dir)

    fun videoCount(context: Context): Int {
        val pickedDir = pickedDir(context)
        pickedDir ?: return 0
        var toReturn = 0
        countSafVideoFiles(arrayOf(pickedDir)) {
            toReturn++
        }
        return toReturn
    }

    val contentString get() = destinationString.substringAfter("content://")
}