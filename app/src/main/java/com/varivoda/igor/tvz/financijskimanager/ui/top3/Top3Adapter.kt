package com.varivoda.igor.tvz.financijskimanager.ui.top3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.model.CategoryDTO

class Top3Adapter : RecyclerView.Adapter<Top3Adapter.CategoryViewHolder>(){

    private var list = listOf<CategoryDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(list[position],position)
    }


    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val recyclerCategory: TextView = itemView.findViewById(R.id.RecyclerCategory)
        val recyclerQuantity: TextView = itemView.findViewById(R.id.RecyclerQuantity)
        val iconTopTen: ImageView = itemView.findViewById(R.id.iconTopTen)

        fun bind(categoryDTO: CategoryDTO, position: Int){
            recyclerCategory.text = categoryDTO.name
            recyclerQuantity.text = "${categoryDTO.count} kom"
            when(position){
                0 -> iconTopTen.setImageResource(R.drawable.ic_gold)
                1 -> iconTopTen.setImageResource(R.drawable.ic_silver)
                2 -> iconTopTen.setImageResource(R.drawable.ic_bronze)
            }
        }

        companion object{
            fun create(parent: ViewGroup): CategoryViewHolder{
                return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.top3_item,parent,false))
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun setListAndInvalidate(all: List<CategoryDTO>){
        this.list = all
        notifyDataSetChanged()
    }
}