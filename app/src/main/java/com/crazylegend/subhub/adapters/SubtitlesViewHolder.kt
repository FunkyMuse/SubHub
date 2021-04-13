package com.crazylegend.subhub.adapters

import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.recyclerview.context
import com.crazylegend.subhub.R
import com.crazylegend.subhub.databinding.ItemviewSubtitleBinding
import com.funkymuse.opensubs.OpenSubtitleItem

class SubtitlesViewHolder(private val binding: ItemviewSubtitleBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(subtitleItem: OpenSubtitleItem?) {
        subtitleItem?.apply {
            binding.subFileName.setPrecomputedText(subFileName)
            binding.subLanguage.setPrecomputedText(context.getString(R.string.sub_language, languageName))
            binding.movieName.setPrecomputedText(context.getString(R.string.movie_name, movieName))
            binding.encoding.setPrecomputedText(context.getString(R.string.encoding, subEncoding))
            binding.downloadsCount.setPrecomputedText(subDownloadsCnt)
            binding.rating.setPrecomputedText(subRating)
        }
    }

}