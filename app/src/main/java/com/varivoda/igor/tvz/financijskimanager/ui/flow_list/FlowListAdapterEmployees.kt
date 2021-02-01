package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Employee
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeDTO

class FlowListAdapterEmployees(private val openPopup: (EmployeeDTO) -> Unit, private val viewModel: FlowListViewModel) : ListAdapter<EmployeeDTO,FlowListViewHolder>(EmployeeDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowListViewHolder {
        return FlowListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FlowListViewHolder, position: Int) {
        holder.bindEmployee(getItem(position))
        holder.itemView.setOnLongClickListener{
            openPopup.invoke(getItem(position))
            true
        }
    }

    class EmployeeDiffCallback : DiffUtil.ItemCallback<EmployeeDTO>(){
        override fun areItemsTheSame(oldItem: EmployeeDTO, newItem: EmployeeDTO): Boolean {
            return oldItem.id == newItem.id && oldItem.locationId == newItem.locationId && oldItem.storeId == newItem.storeId
        }

        override fun areContentsTheSame(oldItem: EmployeeDTO, newItem: EmployeeDTO): Boolean {
            return oldItem.id == newItem.id && oldItem.locationId == newItem.locationId && oldItem.storeId == newItem.storeId
                    && oldItem.employeeName == newItem.employeeName && oldItem.employeeLastName == newItem.employeeLastName
        }

    }

    fun deleteEmployee(position: Int){
        val id = getItem(position).id
        viewModel.deleteEmployee(id)
    }

}