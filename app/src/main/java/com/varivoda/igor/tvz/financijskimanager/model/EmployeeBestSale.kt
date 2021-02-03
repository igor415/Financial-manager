package com.varivoda.igor.tvz.financijskimanager.model

data class EmployeeBestSale(
    val invoiceId: Int,
    val employeeId: Int,
    val name: String?,
    val surname: String,
    val total: String,
    val paymentMethodName: String,
    val date: String,
    val time: String,
    val storeName: String
)