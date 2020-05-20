package com.crazylegend.subhub.adapters.folderSources

import androidx.lifecycle.LifecycleCoroutineScope
import com.crazylegend.kotlinextensions.abstracts.AbstractViewBindingAdapter
import com.crazylegend.subhub.databinding.ItemviewPickedDirBinding
import com.crazylegend.subhub.pickedDirs.PickedDirModel


/**
 * Created by crazy on 11/28/19 to long live and prosper !
 */
//extract the classes to separate files

/**
 * Template created by Hristijan to live long and prosper.
 */

class PickedDirsAdapter(private val lifecycleScope: LifecycleCoroutineScope) :
        AbstractViewBindingAdapter<PickedDirModel, PickedDirViewHolder, ItemviewPickedDirBinding>(PickedDirViewHolder::class.java, ItemviewPickedDirBinding::inflate) {

    override fun bindItems(item: PickedDirModel, holder: PickedDirViewHolder, position: Int, itemCount: Int) {
        holder.bind(item, lifecycleScope)
    }
}


