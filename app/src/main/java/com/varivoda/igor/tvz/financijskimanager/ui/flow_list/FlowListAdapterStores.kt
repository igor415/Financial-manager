package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store

class FlowListAdapterStores : ListAdapter<Store,FlowListViewHolder>(StoreDiffCallback()){

    class StoreDiffCallback : DiffUtil.ItemCallback<Store>(){
        override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowListViewHolder {
        return FlowListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FlowListViewHolder, position: Int) {
        holder.bindStore(getItem(position))
    }
}