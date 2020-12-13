package com.varivoda.igor.tvz.financijskimanager.ui.inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.InventoryItem

class InventoryAdapter : ListAdapter<InventoryItem,InventoryAdapter.InventoryViewHolder>(DiffCallbackItem()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        return InventoryViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val storeName: TextView = itemView.findViewById(R.id.storeName)
        val inventoryDate: TextView = itemView.findViewById(R.id.inventoryDate)
        val fullName: TextView = itemView.findViewById(R.id.fullName)

        fun bind(inventoryItem: InventoryItem){
            inventoryDate.text = inventoryItem.date
            storeName.text = inventoryItem.storeName
            fullName.text = inventoryItem.fullName
        }

        companion object{
            fun create(parent: ViewGroup): InventoryViewHolder{
                return InventoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.inventory_item,parent,false))
            }
        }
    }

    class DiffCallbackItem: DiffUtil.ItemCallback<InventoryItem>(){
        override fun areItemsTheSame(oldItem: InventoryItem, newItem: InventoryItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: InventoryItem, newItem: InventoryItem): Boolean {
            return oldItem.id == newItem.id && newItem.date == oldItem.date && newItem.storeName == oldItem.storeName
        }

    }

}