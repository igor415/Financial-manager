package com.varivoda.igor.tvz.financijskimanager.ui.bills

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.model.BillDTO

class BillAdapter : RecyclerView.Adapter<BillViewHolder>(){

    var bills = mutableListOf<BillDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        return BillViewHolder.create(parent)
    }

    override fun getItemCount(): Int = bills.size

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        holder.bind(bills[position])
    }

    fun setItems(bills: List<BillDTO>){
        this.bills = bills as MutableList<BillDTO>
        notifyDataSetChanged()
    }


}