package com.varivoda.igor.tvz.financijskimanager.ui.menu

import androidx.recyclerview.widget.DiffUtil

class MenuDiffCallback: DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}