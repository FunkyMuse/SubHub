package com.crazylegend.subhub.adapters.subtitles

import com.crazylegend.subhub.R
import com.crazylegend.subhub.core.AbstractAdapter
import com.masterwok.opensubs.models.OpenSubtitleItem


/**
 * Created by hristijan on 9/12/19 to long live and prosper !
 */
//extract the classes to separate files

/**
 * Template created by Hristijan to live long and prosper.
 */

class SubtitlesAdapter : AbstractAdapter<OpenSubtitleItem, SubtitlesViewHolder>(SubtitlesDiffUtil(), SubtitlesViewHolder::class.java) {

    override val getLayout: Int
        get() = R.layout.itemview_subtitle

    override fun bindItems(item: OpenSubtitleItem, holder: SubtitlesViewHolder, position: Int) {
        holder.bind(item)
    }

}

