package com.example.module_14

import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class MapAdapterDeligate( private val onItemClicked: (position: Int ) -> Unit): AbsListItemAdapterDelegate<Map, Map, MapAdapterDeligate.MapHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): MapHolder {
        return  MapHolder(
            parent.inflate(R.layout.item_map),
            onItemClicked
        )
    }

    override fun isForViewType(item: Map, items: MutableList<Map>, position: Int): Boolean {
        return true
    }

    override fun onBindViewHolder(
        item: Map,
        holder: MapHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class MapHolder(
        view: View,
        onItemClicked: (position: Int) -> Unit
    ): BaseMapHolder(view, onItemClicked) {
        init {
        }

        fun bind(map: Map) {
            bindMainInfo(map.id, map.map, map.avatarLink, map.time )
        }

    }


}