package com.crazylegend.subhub.adapters

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.glide.loadImgNoCache
import com.crazylegend.kotlinextensions.memory.toFileSizeString
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.kotlinextensions.views.setPrecomputedTextOrHide
import com.crazylegend.subhub.R
import com.crazylegend.subhub.databinding.ItemviewVideoBinding
import com.crazylegend.subhub.dtos.LocalVideoItem


class LocalVideoViewHolder(private val binding: ItemviewVideoBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(item: LocalVideoItem, showDivider: Boolean) {
        binding.divider.isVisible = !showDivider
        if (item.contentUri == null) {
            binding.videoImage.loadImgNoCache(R.drawable.app_logo)
        } else {
            binding.videoImage.loadImgNoCache(item.contentUri)
        }
        binding.videoName.setPrecomputedTextOrHide(item.displayName)
        binding.videoSize.setPrecomputedText(item.videoSize.toFileSizeString().toString())
    }

}