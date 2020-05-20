package com.crazylegend.subhub.adapters.localVideos

import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.glide.loadImgNoCache
import com.crazylegend.kotlinextensions.memory.toFileSizeString
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.kotlinextensions.views.setPrecomputedTextOrHide
import com.crazylegend.subhub.databinding.ItemviewVideoBinding
import com.crazylegend.subhub.dtos.LocalVideoItem


class LocalVideoViewHolder(private val binding: ItemviewVideoBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(item: LocalVideoItem) {
        binding.videoImage.loadImgNoCache(item.photoUri)
        binding.videoName.setPrecomputedTextOrHide(item.videoName)
        binding.videoSize.setPrecomputedText(item.videoSize.toFileSizeString().toString())
    }

}