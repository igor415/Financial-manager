package com.varivoda.igor.tvz.financijskimanager.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Bill",
    foreignKeys = [ForeignKey(
        entity = Employee::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("employeeId"),
        onDelete = CASCADE
    ), ForeignKey(
        entity = Store::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("storeId"),
        onDelete = CASCADE
    ), ForeignKey(
        entity = Customer::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("customerId"),
        onDelete = CASCADE
    )],
    indices = [Index("employeeId"), Index("storeId"), Index("customerId")]
)
data class Bill(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var employeeId: Int = 0,
    var storeId: Int = 0,
    var customerId: Int = 0,
    var date: String? = null
)