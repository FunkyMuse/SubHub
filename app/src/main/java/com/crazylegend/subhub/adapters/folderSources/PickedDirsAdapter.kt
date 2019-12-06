package com.crazylegend.subhub.adapters.folderSources

import androidx.lifecycle.LifecycleCoroutineScope
import com.crazylegend.subhub.R
import com.crazylegend.subhub.core.AbstractAdapter
import com.crazylegend.subhub.pickedDirs.PickedDirModel


/**
 * Created by crazy on 11/28/19 to long live and prosper !
 */
//extract the classes to separate files

/**
 * Template created by Hristijan to live long and prosper.
 */

class PickedDirsAdapter(private val lifecycleScope: LifecycleCoroutineScope) : AbstractAdapter<PickedDirModel, PickedDirViewHolder>(PickedDirDiffUtil(), PickedDirViewHolder::class.java) {

    override val getLayout: Int
        get() = R.layout.itemview_picked_dir

    override fun bindItems(item: PickedDirModel, holder: PickedDirViewHolder, position: Int) {
        holder.bind(item, lifecycleScope)
    }
}


