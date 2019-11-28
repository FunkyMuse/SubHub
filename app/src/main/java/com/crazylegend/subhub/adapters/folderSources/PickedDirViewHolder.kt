package com.crazylegend.subhub.adapters.folderSources

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.subhub.R
import com.crazylegend.subhub.pickedDirs.PickedDirModel
import kotlinx.android.synthetic.main.itemview_picked_dir.view.*

class PickedDirViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: PickedDirModel) {
        itemView.iv_pd_folder_name?.setPrecomputedText(itemView.context.getString(R.string.folder_name, item.name))
        itemView.iv_pd_video_count?.setPrecomputedText(itemView.context.getString(R.string.video_count, item.videoCount(itemView.context)))
    }

}