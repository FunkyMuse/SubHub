package com.crazylegend.subhub.adapters.folderSources

import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.recyclerview.context
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.setPrecomputedText

import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.subhub.R
import com.crazylegend.subhub.databinding.ItemviewPickedDirBinding
import com.crazylegend.subhub.pickedDirs.PickedDirModel

class PickedDirViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemviewPickedDirBinding.bind(itemView)

    fun bind(item: PickedDirModel, lifecycleScope: LifecycleCoroutineScope) {
        binding.folderName.setPrecomputedText(context.getString(R.string.folder_name, item.name))

        if (item.videoCount == null) {
            binding.videoCount.setPrecomputedText("")
            binding.videoCount.gone()
            item.videoCount(context, lifecycleScope, binding.videoCountProgress) {
                binding.videoCount.visible()
                binding.videoCount.setPrecomputedText(context.getString(R.string.video_count, it))
            }
        } else {
            binding.videoCountProgress.gone()
            binding.videoCount.visible()
            binding.videoCount.setPrecomputedText(context.getString(R.string.video_count, item.videoCount
                    ?: 0))
        }
    }

}