package com.varivoda.igor.tvz.financijskimanager.ui.check_inventory

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.model.ProductStockDTO

class CheckInventoryAdapter : RecyclerView.Adapter<CheckInventoryAdapter.ViewHolder>(){
    var items = mutableListOf<ProductStockDTO>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val product: TextView = itemView.findViewById(R.id.product)
        val quantity: EditText = itemView.findViewById(R.id.quantity)

        fun bind(productStockDTO: ProductStockDTO){
            product.text = productStockDTO.productName
            quantity.setText(productStockDTO.quantity.toString())
        }

        companion object{
            fun create(parent: ViewGroup): ViewHolder{
                return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.check_inventory_item, parent, false))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.quantity.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                items[position].quantity = holder.quantity.text.toString().toIntOrNull() ?: 0
            }
        })

        /*holder.quantity.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                view?.clearFocus()
                items[position].quantity = holder.quantity.text.toString().toIntOrNull() ?: 0
                true
            } else {
                false
            }
        }*/

    }

    fun setItemsValue(list: List<ProductStockDTO>){
        items = list as MutableList<ProductStockDTO>
        notifyDataSetChanged()
    }
}