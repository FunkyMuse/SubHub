package com.crazylegend.subhub.adapters.subtitles

import androidx.recyclerview.widget.DiffUtil
import com.masterwok.opensubs.models.OpenSubtitleItem

class SubtitlesDiffUtil : DiffUtil.ItemCallback<OpenSubtitleItem>() {
    override fun areItemsTheSame(oldItem: OpenSubtitleItem, newItem: OpenSubtitleItem): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: OpenSubtitleItem, newItem: OpenSubtitleItem): Boolean {
        return false
    }
}