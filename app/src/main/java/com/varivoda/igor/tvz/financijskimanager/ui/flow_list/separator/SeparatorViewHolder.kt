package com.varivoda.igor.tvz.financijskimanager.ui.flow_list.separator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.R

class SeparatorViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){

    private val description: TextView = itemView.findViewById(R.id.separator_description)

    fun bind(text: String){
        description.text = text
    }

    companion object{
        fun create(parent: ViewGroup) : SeparatorViewHolder{
            return SeparatorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.separator_view_item,parent,false))
        }
    }
}