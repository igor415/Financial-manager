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
import com.varivoda.igor.tvz.financijskimanager.model.DataOnBill
import com.varivoda.igor.tvz.financijskimanager.model.ProductStockDTO

class CheckInventoryAdapter(private val dataOnBill: Boolean) : RecyclerView.Adapter<CheckInventoryAdapter.ViewHolder>(){
    var items = mutableListOf<ProductStockDTO>()

    var products = mutableListOf<DataOnBill>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val product: TextView = itemView.findViewById(R.id.product)
        val quantity: EditText = itemView.findViewById(R.id.quantity)

        fun bind(productStockDTO: ProductStockDTO){
            product.text = productStockDTO.productName
            quantity.setText(productStockDTO.quantity.toString())
        }

        fun bindDataOnBill(dataOnBill: DataOnBill){
            product.text = dataOnBill.productName
            quantity.setText(dataOnBill.quantity.toString())
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

    override fun getItemCount(): Int {
        return if(dataOnBill){
            products.size
        }else{
            items.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(dataOnBill){
            holder.bindDataOnBill(products[position])
            holder.itemView.setOnClickListener {
                if(products[position].selected){
                    holder.itemView.setBackgroundResource(R.drawable.white_background_wrapper)
                    products[position].selected = false
                }else{
                    holder.itemView.setBackgroundResource(R.drawable.background_wrapper)
                    products[position].selected = true
                }
            }
        }else{
            holder.bind(items[position])
        }

        holder.quantity.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(dataOnBill){
                    products[position].quantity = holder.quantity.text.toString().toIntOrNull() ?: 0
                }else{
                    items[position].quantity = holder.quantity.text.toString().toIntOrNull() ?: 0
                }

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

    fun setProductsValue(list: List<DataOnBill>){
        products = list as MutableList<DataOnBill>
        notifyDataSetChanged()
    }
}