package com.varivoda.igor.tvz.financijskimanager.ui.menu

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MenuListAdapter(private val items: List<String>): RecyclerView.Adapter<ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

}