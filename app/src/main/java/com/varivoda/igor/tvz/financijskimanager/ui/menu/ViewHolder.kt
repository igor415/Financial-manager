package com.varivoda.igor.tvz.financijskimanager.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.databinding.MenuListItemBinding

class ViewHolder private constructor(private var binding: MenuListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        menuItem: String,
        clickListener: MenuItemClickListener
    ){
        binding.item = menuItem
        binding.executePendingBindings()
        binding.clickListener = clickListener
    }

    companion object{
        fun create(parent: ViewGroup): ViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = MenuListItemBinding.inflate(inflater,parent,false)
            return ViewHolder(binding)
        }
    }
}