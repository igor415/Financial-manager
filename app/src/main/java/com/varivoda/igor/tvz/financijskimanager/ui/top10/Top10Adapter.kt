package com.varivoda.igor.tvz.financijskimanager.ui.top10

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.databinding.TopSellersItemBinding
import com.varivoda.igor.tvz.financijskimanager.model.ProductDTO

class Top10Adapter(val clickListener: Top10Listener) : ListAdapter<ProductDTO, Top10Adapter.ViewHolder>(Top10DiffCallback()){


    class ViewHolder private constructor(val binding: TopSellersItemBinding) : RecyclerView.ViewHolder(binding.root) {


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TopSellersItemBinding
                    .inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(productDTO: ProductDTO,clickListener: Top10Listener){
            binding.productDTO = productDTO
            binding.listener = clickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),clickListener)
    }

    class Top10DiffCallback: DiffUtil.ItemCallback<ProductDTO>(){
        override fun areItemsTheSame(oldItem: ProductDTO, newItem: ProductDTO): Boolean {
            return oldItem.productName == newItem.productName
        }

        override fun areContentsTheSame(oldItem: ProductDTO, newItem: ProductDTO): Boolean {
            return oldItem == newItem
        }

    }
}

class Top10Listener(val clickListener: (productDTO: ProductDTO) -> Unit) {
    fun onClick(productDTO: ProductDTO) = clickListener(productDTO)
}