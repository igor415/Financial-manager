package com.varivoda.igor.tvz.financijskimanager.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.County

class ListCountyAdapter() : PagingDataAdapter<County, RecyclerView.ViewHolder>(COUNTY_COMPARATOR){

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ListViewHolder).bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListViewHolder.create(parent)
    }

    companion object {
        private val COUNTY_COMPARATOR = object : DiffUtil.ItemCallback<County>() {
            override fun areItemsTheSame(oldItem: County, newItem: County): Boolean {
                return oldItem.countyId == newItem.countyId
            }

            override fun areContentsTheSame(oldItem: County, newItem: County): Boolean =
                oldItem == newItem
        }
    }


}