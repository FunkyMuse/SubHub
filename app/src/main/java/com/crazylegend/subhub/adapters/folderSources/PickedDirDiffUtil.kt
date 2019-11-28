package com.crazylegend.subhub.adapters.folderSources

import androidx.recyclerview.widget.DiffUtil
import com.crazylegend.subhub.pickedDirs.PickedDirModel

class PickedDirDiffUtil : DiffUtil.ItemCallback<PickedDirModel>() {
    override fun areItemsTheSame(oldItem: PickedDirModel, newItem: PickedDirModel) = oldItem == newItem

    override fun areContentsTheSame(oldItem: PickedDirModel, newItem: PickedDirModel) = oldItem == newItem
}