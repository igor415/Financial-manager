package com.varivoda.igor.tvz.financijskimanager.model

data class EmployeeDTO(
    var id: Int = 0,
    var employeeName: String,
    var employeeLastName: String,
    var address: String? = null,
    var storeId: Int,
    var locationId: Int,
    var storeName: String
)