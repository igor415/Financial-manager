package com.varivoda.igor.tvz.financijskimanager.ui.top10

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.databinding.TopSellersItemBinding
import com.varivoda.igor.tvz.financijskimanager.model.ProductDTO
import kotlinx.android.synthetic.main.top_sellers_item.view.*

class Top10Adapter(val clickListener: Top10Listener) : RecyclerView.Adapter<Top10Adapter.ViewHolder>(){

    var list = mutableListOf<ProductDTO>()

    class ViewHolder private constructor(val binding: TopSellersItemBinding) : RecyclerView.ViewHolder(binding.root) {


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TopSellersItemBinding
                    .inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(productDTO: ProductDTO,clickListener: Top10Listener,position: Int){
            binding.productDTO = productDTO
            binding.position = position
            binding.listener = clickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(position){
            0 -> holder.binding.goldImage.setBackgroundResource(R.drawable.ic_gold)
            1 -> holder.binding.goldImage.setBackgroundResource(R.drawable.ic_silver)
            2 -> holder.binding.goldImage.setBackgroundResource(R.drawable.ic_bronze)
            else -> holder.binding.goldImage.visibility = View.INVISIBLE
        }
        holder.bind(list[position],clickListener,position)
    }

  /*  class Top10DiffCallback: DiffUtil.ItemCallback<ProductDTO>(){
        override fun areItemsTheSame(oldItem: ProductDTO, newItem: ProductDTO): Boolean {
            return oldItem.productName == newItem.productName
        }

        override fun areContentsTheSame(oldItem: ProductDTO, newItem: ProductDTO): Boolean {
            return oldItem == newItem
        }

    }*/

    override fun getItemCount(): Int = list.size

    fun setListValue(list: List<ProductDTO>){
        this.list = list as MutableList<ProductDTO>
        notifyDataSetChanged()
    }
}

class Top10Listener(val clickListener: (productDTO: ProductDTO) -> Unit) {
    fun onClick(productDTO: ProductDTO) = clickListener(productDTO)
}