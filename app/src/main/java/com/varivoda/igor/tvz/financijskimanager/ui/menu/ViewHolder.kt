package com.varivoda.igor.tvz.financijskimanager.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.R

class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val menuItem: TextView = itemView.findViewById(R.id.menuItem)

    fun bind(menuItem: String){
        this.menuItem.text = menuItem
    }

    companion object{
        fun create(parent: ViewGroup): ViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            return ViewHolder(inflater.inflate(R.layout.menu_list_item,parent,false))
        }
    }
}