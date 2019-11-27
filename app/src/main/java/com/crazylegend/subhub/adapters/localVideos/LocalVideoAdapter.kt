package com.crazylegend.subhub.adapters.localVideos

import com.crazylegend.subhub.R
import com.crazylegend.subhub.core.AbstractAdapter


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */

/**
 * Template created by Hristijan to live long and prosper.
 */

class LocalVideoAdapter : AbstractAdapter<LocalVideoItem, LocalVideoViewHolder>(LocalVideoDiffUtil(), LocalVideoViewHolder::class.java) {

    override val getLayout: Int
        get() = R.layout.itemview_video

    override fun bindItems(item: LocalVideoItem, holder: LocalVideoViewHolder, position: Int) {
        holder.bind(item)
    }

}

