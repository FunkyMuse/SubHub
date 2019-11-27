package com.crazylegend.subhub.adapters.localVideos

import androidx.recyclerview.widget.DiffUtil

class LocalVideoDiffUtil : DiffUtil.ItemCallback<LocalVideoItem>() {
    override fun areItemsTheSame(oldItem: LocalVideoItem, newItem: LocalVideoItem) = oldItem == newItem

    override fun areContentsTheSame(oldItem: LocalVideoItem, newItem: LocalVideoItem) = oldItem == newItem
}