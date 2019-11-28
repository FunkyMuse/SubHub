package com.crazylegend.subhub.adapters.localVideos

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.glide.loadImgNoCache
import com.crazylegend.kotlinextensions.memory.toFileSizeString
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.kotlinextensions.views.setPrecomputedTextOrHide
import kotlinx.android.synthetic.main.itemview_video.view.*

class LocalVideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: LocalVideoItem) {
        itemView.iv_video_img?.loadImgNoCache(item.photoUri)
        itemView.iv_video_name?.setPrecomputedTextOrHide(item.videoName)
        itemView.iv_video_size?.setPrecomputedText(item.videoSize.toFileSizeString().toString())
    }

}