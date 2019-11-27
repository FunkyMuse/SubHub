package com.crazylegend.subhub.adapters.subtitles

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.subhub.R
import com.masterwok.opensubs.models.OpenSubtitleItem
import kotlinx.android.synthetic.main.itemview_subtitle.view.*

class SubtitlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val subFileName = itemView.is_subFileName
    private val subLanguage = itemView.is_subLanguage
    private val movieName = itemView.is_movieName
    private val downloadsCount = itemView.is_downloadsCount
    private val rating = itemView.is_rating

    fun bind(subtitleItem: OpenSubtitleItem?) {
        subtitleItem?.apply {
            subFileName?.setPrecomputedText(SubFileName)
            subLanguage?.setPrecomputedText(itemView.context.getString(R.string.sub_language, LanguageName))
            movieName?.setPrecomputedText(itemView.context.getString(R.string.movie_name, MovieName))
            downloadsCount?.setPrecomputedText(SubDownloadsCnt)
            rating?.setPrecomputedText(SubRating)
        }
    }

}