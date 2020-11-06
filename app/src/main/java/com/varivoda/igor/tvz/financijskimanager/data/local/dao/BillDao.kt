package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Bill
import com.varivoda.igor.tvz.financijskimanager.model.BillDTO

@Dao
interface BillDao {

    @Query("""select x.id as id, x.money as money, x.date as date, x.storeName as storeName  from 
            (select b.id,b.date, sum(pob.quantity*p.price) as money, s.storeName 
            from Bill b join ProductsOnBill pob on b.id = pob.billId
            join Product p on p.id = pob.productId
            join Store s on b.storeId = s.id
            where strftime('%m',b.date)=:month and strftime('%Y',b.date)=:year 
            group by b.id,b.date order by money desc) as x""")
    fun getBills(month: String, year: String): DataSource.Factory<Int,BillDTO>

    @Query("SELECT * FROM Bill")
    fun get(): List<Bill>
}