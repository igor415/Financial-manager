package com.varivoda.igor.tvz.financijskimanager.data.local.remote.model

import com.varivoda.igor.tvz.financijskimanager.data.local.entity.*

data class AllData (
    val counties: List<County>,
    val locations: List<Location>,
    val stores: List<Store>,
    val customers: List<Customer>,
    val employees: List<Employee>,
    val products: List<Product>,
    val categories: List<Category>,
    val paymentMethods: List<PaymentMethod>,
    val stockData: List<StockData>,
    val inventoryItems: List<InventoryItem>,
    val bills: List<Bill>,
    val productsOnBills: List<ProductsOnBill>
)