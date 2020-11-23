package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product

class FlowListAdapterProducts(private val openPopup: (Product) -> Unit,private val viewModel: FlowListViewModel) : PagingDataAdapter<Product, FlowListViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowListViewHolder {
        return FlowListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FlowListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
        holder.itemView.setOnLongClickListener {
            getItem(position)?.let { it1 -> openPopup.invoke(it1) }
            true
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    fun deleteProduct(position: Int){
        val id = getItem(position)?.id
        if (id != null) {
            viewModel.deleteProduct(id)
        }
    }

}