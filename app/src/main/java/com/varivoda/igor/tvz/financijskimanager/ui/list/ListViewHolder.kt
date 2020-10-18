package com.varivoda.igor.tvz.financijskimanager.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.County
import kotlinx.android.synthetic.main.menu_list_item.view.*

class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    private val menuItem = itemView.menuItem

    fun bind(
        county: County,
        clickListener: ListItemClickListener
    ){
        menuItem.text = county.countyName
    }
    companion object{
        fun create(parent: ViewGroup): ListViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_list_item,parent,false)
            return ListViewHolder(view)
        }
    }

}