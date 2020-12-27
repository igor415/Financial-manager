package com.varivoda.igor.tvz.financijskimanager.ui.stock

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.model.ProductStockDTO

class StockAdapter(private val showInfo: (ProductStockDTO) -> Unit, private val isPopupLayout: Boolean) : ListAdapter<ProductStockDTO, StockAdapter.StockViewHolder>(StockDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        return StockViewHolder.create(parent, isPopupLayout)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(getItem(position),isPopupLayout)
        holder.info.setOnClickListener {
            showInfo.invoke(getItem(position))
        }
    }

    class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val quantity: TextView = itemView.findViewById(R.id.quantity)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val info: ImageView = itemView.findViewById(R.id.info)

        fun bind(productStockDTO: ProductStockDTO, isPopupLayout: Boolean){
            productName.text = productStockDTO.productName
            quantity.text = productStockDTO.quantity.toString()
            if(!isPopupLayout) {
                if(productStockDTO.yellow){
                    productName.setTextColor(Color.parseColor("#fdaa29"))
                    quantity.setTextColor(Color.parseColor("#fdaa29"))
                    info.setImageResource(R.drawable.ic_baseline_info_24_yellow)
                }else if(productStockDTO.quantity < 5){
                    productName.setTextColor(Color.parseColor("#a83232"))
                    quantity.setTextColor(Color.parseColor("#a83232"))
                    info.setImageResource(R.drawable.ic_baseline_info_24_red)
                }else{
                    productName.setTextColor(Color.parseColor("#808088"))
                    quantity.setTextColor(Color.parseColor("#808080"))
                    info.setImageResource(R.drawable.ic_baseline_info_24)
                }
            }
        }

        companion object{
            fun create(parent: ViewGroup, isPopupLayout: Boolean): StockViewHolder{
                return if(isPopupLayout){
                    StockViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.info_item,parent,false))
                }else{
                    StockViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_item,parent,false))
                }

            }
        }
    }

    class StockDiffCallback: DiffUtil.ItemCallback<ProductStockDTO>(){
        override fun areItemsTheSame(oldItem: ProductStockDTO, newItem: ProductStockDTO): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProductStockDTO, newItem: ProductStockDTO): Boolean {
            return oldItem == newItem
        }

    }


}