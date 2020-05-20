package com.crazylegend.subhub.adapters.subtitles

import com.crazylegend.kotlinextensions.abstracts.AbstractViewBindingAdapter
import com.crazylegend.subhub.databinding.ItemviewSubtitleBinding
import com.masterwok.opensubs.models.OpenSubtitleItem


/**
 * Created by hristijan on 9/12/19 to long live and prosper !
 */
class SubtitlesAdapter : AbstractViewBindingAdapter<OpenSubtitleItem, SubtitlesViewHolder, ItemviewSubtitleBinding>(SubtitlesViewHolder::class.java,
        ItemviewSubtitleBinding::inflate, areContentsTheSameCallback = { _, _ -> false }, areItemsTheSameCallback = { _, _ -> false }) {

    override fun bindItems(item: OpenSubtitleItem, holder: SubtitlesViewHolder, position: Int, itemCount: Int) {
        holder.bind(item)
    }
}

