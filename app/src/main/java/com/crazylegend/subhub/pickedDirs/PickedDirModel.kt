package com.crazylegend.subhub.pickedDirs

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.crazylegend.kotlinextensions.coroutines.defaultDispatcher
import com.crazylegend.kotlinextensions.coroutines.withMainContext
import com.crazylegend.kotlinextensions.ui.ColorProgressBar
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.subhub.utils.countSafVideoFiles
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch

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

    @Ignore
    @IgnoredOnParcel
    var videoCount: Int? = null

    fun videoCount(context: Context, lifecycleScope: LifecycleCoroutineScope, colorProgressBar: ColorProgressBar, count: (count: Int) -> Unit = {}) {
        colorProgressBar.visible()
        lifecycleScope.launch(defaultDispatcher) {
            val pickedDir = pickedDir(context)
            pickedDir ?: withMainContext {
                videoCount = 0
                colorProgressBar.gone()
            }
            var toReturn = 0
            countSafVideoFiles(arrayOf(pickedDir)) {
                toReturn++
            }
            withMainContext {
                videoCount = toReturn
                count(toReturn)
                colorProgressBar.gone()
            }
        }
    }

}