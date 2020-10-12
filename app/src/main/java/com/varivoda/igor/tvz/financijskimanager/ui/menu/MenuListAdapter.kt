package com.varivoda.igor.tvz.financijskimanager.ui.menu

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class MenuListAdapter(val clickListener: MenuItemClickListener): ListAdapter<String,ViewHolder>(MenuDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),clickListener)
        //private val items: List<String>
        //items[position]
    }

}