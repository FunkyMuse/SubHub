package com.crazylegend.subhub.di.providers


import com.crazylegend.recyclerview.generateRecycler
import com.crazylegend.subhub.adapters.LanguageViewHolder
import com.crazylegend.subhub.adapters.LocalVideoViewHolder
import com.crazylegend.subhub.adapters.SubtitlesViewHolder
import com.crazylegend.subhub.databinding.ItemviewLanguageBinding
import com.crazylegend.subhub.databinding.ItemviewSubtitleBinding
import com.crazylegend.subhub.databinding.ItemviewVideoBinding
import com.crazylegend.subhub.dtos.LanguageItem
import com.crazylegend.subhub.dtos.LocalVideoItem
import com.funkymuse.opensubs.OpenSubtitleItem
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject


/**
 * Created by crazy on 5/2/20 to long live and prosper !
 */

@FragmentScoped
class AdaptersProvider @Inject constructor() {

    val videosAdapter by lazy {
        generateRecycler<LocalVideoItem, LocalVideoViewHolder, ItemviewVideoBinding>(::LocalVideoViewHolder, ItemviewVideoBinding::inflate) { item, holder, position, itemCount ->
            holder.bind(item, position == itemCount - 1)
        }
    }

    val languageAdapter by lazy {
        generateRecycler<LanguageItem, LanguageViewHolder, ItemviewLanguageBinding>(::LanguageViewHolder,
                ItemviewLanguageBinding::inflate) { item, holder, position, itemCount ->
            holder.bind(item, position == itemCount - 1)
        }
    }

    val subtitlesAdapter by lazy {
        generateRecycler<OpenSubtitleItem, SubtitlesViewHolder, ItemviewSubtitleBinding>(::SubtitlesViewHolder,
                ItemviewSubtitleBinding::inflate) { item, holder, _, _ ->
            holder.bind(item)
        }
    }
}