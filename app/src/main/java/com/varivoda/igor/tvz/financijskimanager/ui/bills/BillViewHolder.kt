package com.varivoda.igor.tvz.financijskimanager.ui.bills

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.model.BillDTO
import kotlinx.android.synthetic.main.bill_item.view.*

class BillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val billNumber: TextView = itemView.billNumber
    private val billDate: TextView = itemView.billDate
    private val billTotal: TextView = itemView.billTotal

    @SuppressLint("SetTextI18n")
    fun bind(billDTO: BillDTO){
        billNumber.text = billDTO.id.toString()
        billDate.text = billDTO.date
        billTotal.text = "${billDTO.money} kn"
    }

    companion object{
        fun create(parent: ViewGroup): BillViewHolder{
            return BillViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bill_item,parent,false))
        }
    }
}