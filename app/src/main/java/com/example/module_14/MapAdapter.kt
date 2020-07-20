package com.example.module_14

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class MapAdapter(
    private val onItemClicked: (position: Int ) -> Unit
): AsyncListDifferDelegationAdapter<Map>(MapDiffUtilCallback()) {


    init {
        delegatesManager.addDelegate(MapAdapterDeligate(onItemClicked))

    }

    class MapDiffUtilCallback : DiffUtil.ItemCallback<Map>() {
        override fun areItemsTheSame(oldItem: Map, newItem: Map): Boolean {
            return when {
                oldItem is Map && newItem is Map -> oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Map, newItem: Map): Boolean {
            return oldItem == newItem
        }

    }


    companion object {
        private const val TYPE_PRODUCT = 1
        private const val TYPE_DISH = 2

    }
}
