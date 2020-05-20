package com.crazylegend.subhub.adapters.localVideos

import com.crazylegend.kotlinextensions.abstracts.AbstractViewBindingAdapter
import com.crazylegend.subhub.databinding.ItemviewVideoBinding
import com.crazylegend.subhub.dtos.LocalVideoItem


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */

/**
 * Template created by Hristijan to live long and prosper.
 */

class LocalVideoAdapter : AbstractViewBindingAdapter<LocalVideoItem, LocalVideoViewHolder, ItemviewVideoBinding>(LocalVideoViewHolder::class.java
        , ItemviewVideoBinding::inflate) {

    override fun bindItems(item: LocalVideoItem, holder: LocalVideoViewHolder, position: Int, itemCount: Int) {
        holder.bind(item)
    }

}

