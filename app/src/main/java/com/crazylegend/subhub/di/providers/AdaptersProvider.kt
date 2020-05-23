package com.crazylegend.subhub.di.providers


import com.crazylegend.kotlinextensions.recyclerview.generateRecycler
import com.crazylegend.subhub.adapters.LanguageViewHolder
import com.crazylegend.subhub.adapters.LocalVideoViewHolder
import com.crazylegend.subhub.adapters.SubtitlesViewHolder
import com.crazylegend.subhub.databinding.ItemviewLanguageBinding
import com.crazylegend.subhub.databinding.ItemviewSubtitleBinding
import com.crazylegend.subhub.databinding.ItemviewVideoBinding
import com.crazylegend.subhub.dtos.LanguageItem
import com.crazylegend.subhub.dtos.LocalVideoItem
import com.masterwok.opensubs.models.OpenSubtitleItem
import javax.inject.Inject


/**
 * Created by crazy on 5/2/20 to long live and prosper !
 */

class AdaptersProvider @Inject constructor() {

    val videosAdapter by lazy {
        generateRecycler<LocalVideoItem, LocalVideoViewHolder, ItemviewVideoBinding>(LocalVideoViewHolder::class.java
                , ItemviewVideoBinding::inflate) { item, holder, position, itemCount ->
            holder.bind(item, position == itemCount - 1)
        }
    }

    val languageAdapter by lazy {
        generateRecycler<LanguageItem, LanguageViewHolder, ItemviewLanguageBinding>(LanguageViewHolder::class.java,
                ItemviewLanguageBinding::inflate) { item, holder, _, _ ->
            holder.bind(item)
        }
    }

    val subtitlesAdapter by lazy {
        generateRecycler<OpenSubtitleItem, SubtitlesViewHolder, ItemviewSubtitleBinding>(SubtitlesViewHolder::class.java,
                ItemviewSubtitleBinding::inflate, areContentsTheSameCallback = { _, _ -> false }, areItemsTheSameCallback = { _, _ -> false }) { item, holder, _, _ ->
            holder.bind(item)
        }
    }
}