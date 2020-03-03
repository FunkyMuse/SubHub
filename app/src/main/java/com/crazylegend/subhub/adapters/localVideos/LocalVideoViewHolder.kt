package com.crazylegend.subhub.adapters.localVideos

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.glide.loadImgNoCache
import com.crazylegend.kotlinextensions.memory.toFileSizeString
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.kotlinextensions.views.setPrecomputedTextOrHide
import com.crazylegend.subhub.databinding.ItemviewVideoBinding


class LocalVideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemviewVideoBinding.bind(itemView)

    fun bind(item: LocalVideoItem) {
        binding.videoImage.loadImgNoCache(item.photoUri)
        binding.videoName.setPrecomputedTextOrHide(item.videoName)
        binding.videoSize.setPrecomputedText(item.videoSize.toFileSizeString().toString())
    }

}