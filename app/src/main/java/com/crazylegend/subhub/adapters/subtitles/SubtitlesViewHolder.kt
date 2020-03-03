package com.crazylegend.subhub.adapters.subtitles

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.subhub.R
import com.crazylegend.subhub.databinding.ItemviewSubtitleBinding
import com.masterwok.opensubs.models.OpenSubtitleItem

class SubtitlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemviewSubtitleBinding.bind(itemView)

    fun bind(subtitleItem: OpenSubtitleItem?) {
        subtitleItem?.apply {
            binding.subFileName.setPrecomputedText(SubFileName)
            binding.subLanguage.setPrecomputedText(itemView.context.getString(R.string.sub_language, LanguageName))
            binding.movieName.setPrecomputedText(itemView.context.getString(R.string.movie_name, MovieName))
            binding.downloadsCount.setPrecomputedText(SubDownloadsCnt)
            binding.rating.setPrecomputedText(SubRating)
        }
    }

}