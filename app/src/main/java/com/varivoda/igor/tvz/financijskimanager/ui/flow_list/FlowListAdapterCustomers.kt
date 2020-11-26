package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Customer

class FlowListAdapterCustomers(private val viewModel: FlowListViewModel) : PagingDataAdapter<Customer,FlowListViewHolder>(CustomerDiffCallback()){

    class CustomerDiffCallback : DiffUtil.ItemCallback<Customer>(){
        override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowListViewHolder {
        return FlowListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FlowListViewHolder, position: Int) {
        val customer = getItem(position)
        customer?.let { holder.bindCustomer(customer) }

    }

    fun deleteCustomer(position: Int){
        val id = getItem(position)?.id
        id?.let { viewModel.deleteCustomer(id) }
    }

}