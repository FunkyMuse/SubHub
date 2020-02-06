package com.crazylegend.subhub.adapters.folderSources

import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.setPrecomputedText

import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.subhub.R
import com.crazylegend.subhub.pickedDirs.PickedDirModel
import kotlinx.android.synthetic.main.itemview_picked_dir.view.*

class PickedDirViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    fun bind(item: PickedDirModel, lifecycleScope: LifecycleCoroutineScope) {
        itemView.iv_pd_folder_name?.setPrecomputedText(itemView.context.getString(R.string.folder_name, item.name))

        if (item.videoCount == null) {
            itemView.iv_pd_video_count?.setPrecomputedText("")
            itemView.iv_pd_video_count?.gone()
            item.videoCount(itemView.context, lifecycleScope, itemView.iv_pd_video_count_progress) {
                itemView.iv_pd_video_count?.visible()
                itemView.iv_pd_video_count?.setPrecomputedText(itemView.context.getString(R.string.video_count, it))
            }
        } else {
            itemView.iv_pd_video_count_progress?.gone()
            itemView.iv_pd_video_count?.visible()
            itemView.iv_pd_video_count?.setPrecomputedText(itemView.context.getString(R.string.video_count, item.videoCount
                    ?: 0))
        }
    }

}