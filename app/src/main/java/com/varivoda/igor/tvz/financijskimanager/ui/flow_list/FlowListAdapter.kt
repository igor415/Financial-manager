package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product

class FlowListAdapter : ListAdapter<Product, FlowListViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowListViewHolder {
        return FlowListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FlowListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

}