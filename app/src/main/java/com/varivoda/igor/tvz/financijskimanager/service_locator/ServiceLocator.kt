package com.varivoda.igor.tvz.financijskimanager.service_locator

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Bill
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Customer
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.*
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseInventoryRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.monitoring.ConnectivityAgent
import kotlinx.coroutines.runBlocking

object ServiceLocator{

    private val lock = Any()

    private var database: AppDatabase? = null

    @Volatile
    private var storeRepository: StoreRepository? = null

    @Volatile
    var productRepository: BaseProductRepository? = null
        @VisibleForTesting set

    @Volatile
    private var employeeRepository: EmployeeRepository? = null

    @Volatile
    private var customerRepository: CustomerRepository? = null

    @Volatile
    private var billRepository: BillRepository? = null

    @Volatile
    private var loginRepository: LoginRepository? = null

    @Volatile
    private var inventoryRepository: BaseInventoryRepository?= null

    @Volatile
    private var preferences: Preferences? = null

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            productRepository = null
            employeeRepository = null
            billRepository = null
            customerRepository = null
            storeRepository = null
        }
    }

    fun providePreferences(context: Context): Preferences{
        return preferences ?: createPreferences(context)
    }

    fun provideBillRepository(context: Context): BillRepository{
        synchronized(this){
            return billRepository ?: createBillRepository(context)
        }
    }

    private fun createBillRepository(context: Context): BillRepository {
        val new = BillRepository(database ?: createDatabase(context))
        billRepository = new
        return new
    }

    fun provideInventoryRepository(context: Context): BaseInventoryRepository{
        val new = InventoryRepository(database ?: createDatabase(context))
        inventoryRepository = new
        return new
    }

    fun provideLoginRepository(context: Context): LoginRepository{
        val new = LoginRepository(providePreferences(context), ConnectivityAgent(context))
        loginRepository = new
        return new
    }

    private fun createPreferences(context: Context): Preferences {
        val new = Preferences(context)
        preferences = new
        return new
    }

    fun provideStoreRepository(context: Context): StoreRepository{
        synchronized(this){
            return storeRepository ?: createStoreRepository(context)
        }
    }

    fun provideProductRepository(context: Context): BaseProductRepository{
        synchronized(this){
            return productRepository ?: createProductRepository(context)
        }
    }

    fun provideEmployeeRepository(context: Context): EmployeeRepository{
        return employeeRepository ?: createEmployeeRepository(context)
    }

    fun provideCustomerRepository(context: Context): CustomerRepository{
        return customerRepository ?: createCustomerRepository(context)
    }

    private fun createCustomerRepository(context: Context): CustomerRepository {
        val new = CustomerRepository(database ?: createDatabase(context))
        customerRepository = new
        return new
    }

    private fun createEmployeeRepository(context: Context): EmployeeRepository {
        val new = EmployeeRepository(database ?: createDatabase(context))
        employeeRepository = new
        return new
    }

    private fun createProductRepository(context: Context): ProductRepository {
        val new = ProductRepository(database ?: createDatabase(context))
        productRepository = new
        return new
    }

    private fun createStoreRepository(context: Context): StoreRepository {
        val new = StoreRepository(database ?: createDatabase(context))
        storeRepository = new
        return new
    }

    private fun createDatabase(context: Context): AppDatabase {
        val new = Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"appDatabase.db")
            .addCallback(object: RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL("INSERT INTO County VALUES(10000,'Grad Zagreb')")
                    db.execSQL("INSERT INTO County VALUES(51000,'Primorsko-goranska županija')")
                    db.execSQL("INSERT INTO County VALUES(31000,'Osječko-baranjska županija')")
                    db.execSQL("INSERT INTO County VALUES(20003,'Dubrovačka')")
                    db.execSQL("INSERT INTO County VALUES(20004,'Dubrovačka')")
                    db.execSQL("INSERT INTO County VALUES(20005,'hjk')")
                    db.execSQL("INSERT INTO County VALUES(20006,'jk')")
                    db.execSQL("INSERT INTO County VALUES(20007,'ghj')")
                    db.execSQL("INSERT INTO County VALUES(20008,'hjkh')")
                    db.execSQL("INSERT INTO County VALUES(20009,'Dubhrovačka')")
                    db.execSQL("INSERT INTO County VALUES(20010,'Dubjghrovačka')")
                    db.execSQL("INSERT INTO County VALUES(20020,'ghfg')")
                    db.execSQL("INSERT INTO County VALUES(20030,'gfdlč')")
                    db.execSQL("INSERT INTO County VALUES(20040,'sfdjklk')")
                    db.execSQL("INSERT INTO County VALUES(20060,'sfdpšpš')")
                    db.execSQL("INSERT INTO County VALUES(20050,'sfdopop')")
                    db.execSQL("INSERT INTO County VALUES(20070,'sfdioi')")
                    db.execSQL("INSERT INTO County VALUES(20080,'sfduzi')")
                    db.execSQL("INSERT INTO County VALUES(20090,'sfdtuztzu')")
                    db.execSQL("INSERT INTO County VALUES(20041,'sfdrrrr')")
                    db.execSQL("INSERT INTO County VALUES(20042,'sfdeeee')")
                    db.execSQL("INSERT INTO County VALUES(20043,'sfdwww')")
                    db.execSQL("INSERT INTO County VALUES(20044,'sfdqqq')")
                    db.execSQL("INSERT INTO County VALUES(20045,'sfdsadfgg')")
                    db.execSQL("INSERT INTO County VALUES(20046,'sfddsf')")
                    db.execSQL("INSERT INTO County VALUES(20047,'asdsfd')")
                    db.execSQL("INSERT INTO Location VALUES(1,'Zagreb',10000)")
                    db.execSQL("INSERT INTO Location VALUES(2,'Rijeka',51000)")
                    db.execSQL("INSERT INTO Location VALUES(3,'Osijek',31000)")
                    db.execSQL("INSERT INTO Store VALUES(1,'Zitnjak',null,1)")
                    db.execSQL("INSERT INTO Store VALUES(2,'Jankomir',null,1)")
                    db.execSQL("INSERT INTO Store VALUES(3,'Ilica',null,1)")
                    db.execSQL("INSERT INTO Store VALUES(4,'Novi Zagreb',null,1)")
                    db.execSQL("INSERT INTO Store VALUES(5,'Rijeka',null,2)")
                    db.execSQL("INSERT INTO Store VALUES(6,'Osijek',null,3)")
                    db.execSQL("INSERT INTO Category VALUES(1,'Komponente')")
                    db.execSQL("INSERT INTO Category VALUES(2,'Periferija')")
                    db.execSQL("INSERT INTO Category VALUES(3,'Mobiteli')")
                    db.execSQL("INSERT INTO Category VALUES(4,'Software')")
                    db.execSQL("INSERT INTO Category VALUES(5,'Tableti')")
                    db.execSQL("INSERT INTO Category VALUES(6,'Kućni telefoni')")
                    db.execSQL("INSERT INTO Product VALUES(1,'SSD 120GB WD Green',246,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(2,'SSD 256GB Gigabyte',349,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(3,'SSD 240GB Kingston',381,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(4,'Napajanje NaviaTech 400W',170,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(5,'Napajanje Xilence 500W',251,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(6,'Napajanje AKYGA 700W',299,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(7,'Patriot Signature DDR4 2666Mhz 4GB',199,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(8,'Kingston DDR4 2400MHz 8GB',369,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(9,'Memorija G.Skill Aegis 8GB DDR4 3200MHz',349,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(10,'Kingmax Zeus Gaming 16GB DDR4 3000MHz',466,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(11,'Crucial 16GB DDR4 3200 MT/s',724,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(12,'Procesor AMD Ryzen 3 1200',349,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(13,'Procesor Intel Core i3 9100F',722,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(14,'Procesor AMD Ryzen 5 1600 AF',799,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(15,'Procesor Intel core i5 9400F',1249,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(16,'Procesor AMD Ryzen Threadripper',1303,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(17,'Procesor Intel core i5 9600K',2029,1,null)")
                    db.execSQL("INSERT INTO Product VALUES(18,'HP LA2205wg 22\" monitor',498,2,null)")
                    db.execSQL("INSERT INTO Product VALUES(19,'Philips 22\" 223V5LSB2/10 monitor',650,2,null)")
                    db.execSQL("INSERT INTO Product VALUES(20,'Monitor Asus VP247HAE',720,2,null)")
                    db.execSQL("INSERT INTO Product VALUES(21,'Acer Monitor V226HQLBbi 21.5',844,2,null)")
                    db.execSQL("INSERT INTO Product VALUES(22,'Monitor LG 22MK400H 24.0',778,2,null)")
                    db.execSQL("INSERT INTO Product VALUES(23,'LG K50S',1373,3,null)")
                    db.execSQL("INSERT INTO Product VALUES(24,'Xiaomi Redmi Note 8t',1573,3,null)")
                    db.execSQL("INSERT INTO Product VALUES(25,'Sony Xperia L4',1636,3,null)")
                    db.execSQL("INSERT INTO Product VALUES(26,'Huawei P30 Lite',2199,3,null)")
                    db.execSQL("INSERT INTO Product VALUES(27,'Samsung Galaxy A20',1380,3,null)")
                    db.execSQL("INSERT INTO Product VALUES(28,'Windows 10 Professional licenca',724,4,null)")
                    db.execSQL("INSERT INTO Product VALUES(29,'Windows 10 Home',599,4,null)")
                    db.execSQL("INSERT INTO Product VALUES(30,'Microsoft Office 2019 Home',1995,4,null)")
                    db.execSQL("INSERT INTO Product VALUES(31,'Lenovo Tab E7',783,5,null)")
                    db.execSQL("INSERT INTO Product VALUES(32,'Huawei Mediapad M5 Lite',1699,5,null)")
                    db.execSQL("INSERT INTO Product VALUES(33,'Samsung Galaxy Tab T515',2135,5,null)")
                    db.execSQL("INSERT INTO Product VALUES(34,'Acer Enduro T1',3578,5,null)")
                    db.execSQL("INSERT INTO Product VALUES(35,'PANASONIC KX-TS500FXR',115,6,null)")
                    db.execSQL("INSERT INTO Product VALUES(36,'Gigaset A120',209,6,null)")
                    db.execSQL("INSERT INTO Product VALUES(37,'PANASONIC KX-TGC220FXB',299,6,null)")
                    db.execSQL("INSERT INTO Product VALUES(38,'Meanit ST100',79,6,null)")
                    db.execSQL("INSERT INTO Product VALUES(39,'Siemens Gigaset C530',429,6,null)")
                    db.execSQL("INSERT INTO Product VALUES(40,'Motorola DT101',239,6,null)")
                    db.execSQL("INSERT INTO Employee VALUES(1,'Nikola','Bacic',null,1,1)")
                    db.execSQL("INSERT INTO Employee VALUES(2,'Karlo','Krsnik',null,1,1)")
                    db.execSQL("INSERT INTO Employee VALUES(3,'Katarina','Dobrina',null,1,1)")
                    db.execSQL("INSERT INTO Employee VALUES(4,'Nikola','Medvedec',null,2,1)")
                    db.execSQL("INSERT INTO Employee VALUES(5,'Edita','Domijan',null,2,1)")
                    db.execSQL("INSERT INTO Employee VALUES(6,'Iva','Mioc',null,2,1)")
                    db.execSQL("INSERT INTO Employee VALUES(7,'Dominik','Hacek',null,3,1)")
                    db.execSQL("INSERT INTO Employee VALUES(8,'Denis','Pauk',null,3,1)")
                    db.execSQL("INSERT INTO Employee VALUES(9,'Gorana','Bozic',null,3,1)")
                    db.execSQL("INSERT INTO Employee VALUES(10,'Petra','Culjak',null,4,1)")
                    db.execSQL("INSERT INTO Employee VALUES(11,'Davor','Jurinjak',null,4,1)")
                    db.execSQL("INSERT INTO Employee VALUES(12,'Nikola','Cavarovic',null,5,2)")
                    db.execSQL("INSERT INTO Employee VALUES(13,'Stjepan','Kuzmic',null,5,2)")
                    db.execSQL("INSERT INTO Employee VALUES(14,'Ivona','Sup',null,5,2)")
                    db.execSQL("INSERT INTO Employee VALUES(15,'Ivana','Jukic',null,6,3)")
                    db.execSQL("INSERT INTO Employee VALUES(16,'Antonija','Tolic',null,6,3)")
                    db.execSQL("INSERT INTO Customer VALUES(1,'Marin','Maric',null,0,10000)")
                    db.execSQL("INSERT INTO Customer VALUES(2,'Gabriela','Antunovic',null,0,10000)")
                    db.execSQL("INSERT INTO Customer VALUES(3,'Ivo','Peric',null,0,10000)")
                    db.execSQL("INSERT INTO Customer VALUES(4,'Josipa','Budimir',null,0,10000)")
                    db.execSQL("INSERT INTO Customer VALUES(5,'Renata','Blek',null,0,10000)")
                    db.execSQL("INSERT INTO Customer VALUES(6,'Ana','Milic',null,0,31000)")
                    db.execSQL("INSERT INTO Customer VALUES(7,'Tomislav','Knezevic',null,0,31000)")
                    db.execSQL("INSERT INTO Customer VALUES(8,'Stipe','Piskur',null,0,51000)")
                    db.execSQL("INSERT INTO Customer VALUES(9,'Ana','Istuk',null,0,51000)")
                    db.execSQL("INSERT INTO Customer VALUES(10,'Bojan','Gavranovic',null,0,51000)")
                    db.execSQL("INSERT INTO Customer VALUES(11,'Marija','Sestok',null,0,10000)")
                    db.execSQL("INSERT INTO Customer VALUES(12,'Dino','Vlasic',null,0,51000)")
                    db.execSQL("INSERT INTO Customer VALUES(13,'Ante','Kovacic',null,0,10000)")
                    db.execSQL("INSERT INTO Customer VALUES(14,'Matej','Kramaric',null,0,51000)")
                    db.execSQL("INSERT INTO Customer VALUES(15,'Ivana','Jukic',null,0,10000)")
                    db.execSQL("INSERT INTO Customer VALUES(16,'Ana','Krizic',null,0,51000)")
                    db.execSQL("INSERT INTO Customer VALUES(17,'Luka','Gajic',null,0,10000)")
                    db.execSQL("INSERT INTO Customer VALUES(18,'Aleksandar','Gavran',null,0,51000)")
                    db.execSQL("INSERT INTO Customer VALUES(19,'Ana','Lovric',null,0,10000)")
                    db.execSQL("INSERT INTO Customer VALUES(20,'Katarina','Rasic',null,0,51000)")
                    db.execSQL("INSERT INTO Customer VALUES(21,'Antea','Sutalo',null,0,10000)")
                    db.execSQL("INSERT INTO Customer VALUES(22,'Mateo','Lukic',null,0,51000)")
                    db.execSQL("INSERT INTO Customer VALUES(23,'Karlo','Glavas',null,0,10000)")
                    db.execSQL("INSERT INTO BILL VALUES(1,1,1,5,'2020-06-01','11:55')")
                    db.execSQL("INSERT INTO BILL VALUES(2,4,2,4,'2020-06-01','12:55')")
                    db.execSQL("INSERT INTO BILL VALUES(3,7,3,3,'2020-06-01','12:55')")
                    db.execSQL("INSERT INTO BILL VALUES(4,10,4,2,'2020-06-01','13:55')")
                    db.execSQL("INSERT INTO BILL VALUES(5,16,5,8,'2020-06-01','14:55')")
                    db.execSQL("INSERT INTO BILL VALUES(6,13,6,7,'2020-06-01','13:55')")
                    db.execSQL("INSERT INTO BILL VALUES(7,13,6,7,'2020-06-01','14:55')")
                    db.execSQL("INSERT INTO BILL VALUES(8,13,6,7,'2020-06-01','14:55')")
                    db.execSQL("INSERT INTO BILL VALUES(9,13,6,7,'2020-06-30','15:55')")
                    db.execSQL("INSERT INTO BILL VALUES(10,13,6,7,'2020-09-07','11:55')")
                    db.execSQL("INSERT INTO BILL VALUES(11,13,5,6,'2020-09-08','15:55')")
                    db.execSQL("INSERT INTO BILL VALUES(12,13,5,5,'2020-09-09','13:55')")
                    db.execSQL("INSERT INTO BILL VALUES(13,11,5,4,'2020-09-15','11:55')")
                    db.execSQL("INSERT INTO BILL VALUES(14,12,1,3,'2020-09-13','12:55')")
                    db.execSQL("INSERT INTO BILL VALUES(15,14,2,2,'2020-09-12','15:55')")
                    db.execSQL("INSERT INTO BILL VALUES(16,14,1,3,'2020-07-27','12:55')")
                    db.execSQL("INSERT INTO BILL VALUES(17,15,2,4,'2020-07-25','15:55')")
                    db.execSQL("INSERT INTO BILL VALUES(18,16,1,5,'2020-07-22','10:55')")
                    db.execSQL("INSERT INTO BILL VALUES(19,11,2,6,'2020-07-21','08:55')")
                    db.execSQL("INSERT INTO BILL VALUES(20,12,1,8,'2020-07-11','10:55')")
                    db.execSQL("INSERT INTO BILL VALUES(21,14,5,10,'2020-11-28','12:55')")
                    db.execSQL("INSERT INTO BILL VALUES(22,13,4,11,'2020-11-29','08:05')")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(1,1,4,2)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(2,2,1,2)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(3,3,2,3)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(4,4,7,1)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(5,5,8,1)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(6,6,9,3)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(7,6,19,3)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(8,6,18,3)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(9,6,17,3)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(10,10,10,2)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(11,11,11,3)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(12,12,16,3)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(13,13,12,2)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(14,14,15,1)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(15,15,14,1)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(16,16,13,1)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(17,17,6,3)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(18,18,7,2)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(19,19,1,1)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(20,20,1,3)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(21,21,37,1)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(22,21,38,2)")
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(23,22,39,1)")
                    db.execSQL("INSERT INTO InventoryItem VALUES(1,'Jankomir','2020-06-30',1,'Marko Marulic')")
                    db.execSQL("INSERT INTO InventoryItem VALUES(2,'Rijeka','2020-07-30',0,'Ivo Kustra')")
                }
            })
            .fallbackToDestructiveMigration()
            .build()
        database = new
        return new
    }
}