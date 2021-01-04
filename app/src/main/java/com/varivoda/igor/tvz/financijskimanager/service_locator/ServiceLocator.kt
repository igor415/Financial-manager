package com.varivoda.igor.tvz.financijskimanager.service_locator

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.*
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseInventoryRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.monitoring.ConnectivityAgent
import com.varivoda.igor.tvz.financijskimanager.util.clearDbCharKey
import com.varivoda.igor.tvz.financijskimanager.util.getCharKey
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

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
        val new = BillRepository(database ?: createDatabase(context)!!)
        billRepository = new
        return new
    }

    fun provideInventoryRepository(context: Context): BaseInventoryRepository{
        val new = InventoryRepository(database ?: createDatabase(context)!!)
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
        val new = CustomerRepository(database ?: createDatabase(context)!!)
        customerRepository = new
        return new
    }

    private fun createEmployeeRepository(context: Context): EmployeeRepository {
        val new = EmployeeRepository(database ?: createDatabase(context)!!)
        employeeRepository = new
        return new
    }

    private fun createProductRepository(context: Context): ProductRepository {
        val new = ProductRepository(database ?: createDatabase(context)!!)
        productRepository = new
        return new
    }

    private fun createStoreRepository(context: Context): StoreRepository {
        val new = StoreRepository(database ?: createDatabase(context)!!)
        storeRepository = new
        return new
    }

    fun clearDatabase(){
        database = null
        clearDbCharKey()
    }

    fun createDatabase(context: Context, pass: String = "false"): AppDatabase? {
        /*val dbKey = getCharKey(pass.toCharArray(), context)
        if(dbKey.isEmpty()){
            clearDbCharKey()
            return database
        }
        val supportFactory = SupportFactory(SQLiteDatabase.getBytes(dbKey))*/
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
                    db.execSQL("INSERT INTO Store VALUES(1,'Zitnjak',null,'01 7787 044',1)")
                    db.execSQL("INSERT INTO Store VALUES(2,'Jankomir',null,'01 1387 044',1)")
                    db.execSQL("INSERT INTO Store VALUES(3,'Ilica',null,'01 3287 044',1)")
                    db.execSQL("INSERT INTO Store VALUES(4,'Novi Zagreb',null,'01 8587 044',1)")
                    db.execSQL("INSERT INTO Store VALUES(5,'Rijeka',null,'01 6587 044',2)")
                    db.execSQL("INSERT INTO Store VALUES(6,'Osijek',null,'01 4587 044',3)")
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
                    db.execSQL("INSERT INTO ProductsOnBill VALUES(1,1,4,200)")
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
                    db.execSQL("INSERT INTO StockData VALUES(1,1,1,34)")
                    db.execSQL("INSERT INTO StockData VALUES(2,1,2,24)")
                    db.execSQL("INSERT INTO StockData VALUES(3,1,3,24)")
                    db.execSQL("INSERT INTO StockData VALUES(4,1,4,11)")
                    db.execSQL("INSERT INTO StockData VALUES(5,1,5,24)")
                    db.execSQL("INSERT INTO StockData VALUES(6,1,6,24)")
                    db.execSQL("INSERT INTO StockData VALUES(7,1,7,34)")
                    db.execSQL("INSERT INTO StockData VALUES(8,1,8,24)")
                    db.execSQL("INSERT INTO StockData VALUES(9,1,9,14)")
                    db.execSQL("INSERT INTO StockData VALUES(10,1,10,14)")
                    db.execSQL("INSERT INTO StockData VALUES(11,1,11,14)")
                    db.execSQL("INSERT INTO StockData VALUES(12,1,12,11)")
                    db.execSQL("INSERT INTO StockData VALUES(13,1,13,4)")
                    db.execSQL("INSERT INTO StockData VALUES(14,1,14,6)")
                    db.execSQL("INSERT INTO StockData VALUES(15,1,15,15)")
                    db.execSQL("INSERT INTO StockData VALUES(16,1,16,9)")
                    db.execSQL("INSERT INTO StockData VALUES(17,1,17,14)")
                    db.execSQL("INSERT INTO StockData VALUES(18,1,18,24)")
                    db.execSQL("INSERT INTO StockData VALUES(19,1,19,14)")
                    db.execSQL("INSERT INTO StockData VALUES(20,1,20,7)")

                    db.execSQL("INSERT INTO StockData VALUES(21,2,1,34)")
                    db.execSQL("INSERT INTO StockData VALUES(22,2,2,24)")
                    db.execSQL("INSERT INTO StockData VALUES(23,2,3,24)")
                    db.execSQL("INSERT INTO StockData VALUES(24,2,4,24)")
                    db.execSQL("INSERT INTO StockData VALUES(25,2,5,24)")
                    db.execSQL("INSERT INTO StockData VALUES(26,2,6,24)")
                    db.execSQL("INSERT INTO StockData VALUES(27,2,7,34)")
                    db.execSQL("INSERT INTO StockData VALUES(28,2,8,24)")
                    db.execSQL("INSERT INTO StockData VALUES(29,2,9,14)")
                    db.execSQL("INSERT INTO StockData VALUES(30,2,10,14)")
                    db.execSQL("INSERT INTO StockData VALUES(31,2,11,14)")
                    db.execSQL("INSERT INTO StockData VALUES(32,2,12,11)")
                    db.execSQL("INSERT INTO StockData VALUES(33,2,13,4)")
                    db.execSQL("INSERT INTO StockData VALUES(34,2,14,6)")
                    db.execSQL("INSERT INTO StockData VALUES(35,2,15,15)")
                    db.execSQL("INSERT INTO StockData VALUES(36,2,16,9)")
                    db.execSQL("INSERT INTO StockData VALUES(37,2,17,14)")
                    db.execSQL("INSERT INTO StockData VALUES(38,2,18,24)")
                    db.execSQL("INSERT INTO StockData VALUES(39,2,19,14)")
                    db.execSQL("INSERT INTO StockData VALUES(40,2,20,7)")

                    db.execSQL("INSERT INTO StockData VALUES(41,3,1,34)")
                    db.execSQL("INSERT INTO StockData VALUES(42,3,2,24)")
                    db.execSQL("INSERT INTO StockData VALUES(43,3,3,24)")
                    db.execSQL("INSERT INTO StockData VALUES(44,3,4,24)")
                    db.execSQL("INSERT INTO StockData VALUES(45,3,5,24)")
                    db.execSQL("INSERT INTO StockData VALUES(46,3,6,24)")
                    db.execSQL("INSERT INTO StockData VALUES(47,3,7,34)")
                    db.execSQL("INSERT INTO StockData VALUES(48,3,8,24)")
                    db.execSQL("INSERT INTO StockData VALUES(49,3,9,14)")
                    db.execSQL("INSERT INTO StockData VALUES(50,3,10,14)")
                    db.execSQL("INSERT INTO StockData VALUES(51,3,11,14)")
                    db.execSQL("INSERT INTO StockData VALUES(52,3,12,11)")
                    db.execSQL("INSERT INTO StockData VALUES(53,3,13,4)")
                    db.execSQL("INSERT INTO StockData VALUES(54,3,14,6)")
                    db.execSQL("INSERT INTO StockData VALUES(55,3,15,15)")
                    db.execSQL("INSERT INTO StockData VALUES(56,3,16,9)")
                    db.execSQL("INSERT INTO StockData VALUES(57,3,17,14)")
                    db.execSQL("INSERT INTO StockData VALUES(58,3,18,24)")
                    db.execSQL("INSERT INTO StockData VALUES(59,3,19,14)")
                    db.execSQL("INSERT INTO StockData VALUES(60,3,20,7)")

                    db.execSQL("INSERT INTO StockData VALUES(61,4,1,34)")
                    db.execSQL("INSERT INTO StockData VALUES(62,4,2,24)")
                    db.execSQL("INSERT INTO StockData VALUES(63,4,3,24)")
                    db.execSQL("INSERT INTO StockData VALUES(64,4,4,24)")
                    db.execSQL("INSERT INTO StockData VALUES(65,4,5,24)")
                    db.execSQL("INSERT INTO StockData VALUES(66,4,6,24)")
                    db.execSQL("INSERT INTO StockData VALUES(67,4,7,34)")
                    db.execSQL("INSERT INTO StockData VALUES(68,4,8,24)")
                    db.execSQL("INSERT INTO StockData VALUES(69,4,9,14)")
                    db.execSQL("INSERT INTO StockData VALUES(70,4,10,14)")
                    db.execSQL("INSERT INTO StockData VALUES(71,4,11,14)")
                    db.execSQL("INSERT INTO StockData VALUES(72,4,12,11)")
                    db.execSQL("INSERT INTO StockData VALUES(73,4,13,4)")
                    db.execSQL("INSERT INTO StockData VALUES(74,4,14,6)")
                    db.execSQL("INSERT INTO StockData VALUES(75,4,15,15)")
                    db.execSQL("INSERT INTO StockData VALUES(76,4,16,9)")
                    db.execSQL("INSERT INTO StockData VALUES(77,4,17,14)")
                    db.execSQL("INSERT INTO StockData VALUES(78,4,18,24)")
                    db.execSQL("INSERT INTO StockData VALUES(79,4,19,14)")
                    db.execSQL("INSERT INTO StockData VALUES(80,4,20,7)")

                    db.execSQL("INSERT INTO StockData VALUES(81,5,1,34)")
                    db.execSQL("INSERT INTO StockData VALUES(82,5,2,24)")
                    db.execSQL("INSERT INTO StockData VALUES(83,5,3,24)")
                    db.execSQL("INSERT INTO StockData VALUES(84,5,4,24)")
                    db.execSQL("INSERT INTO StockData VALUES(85,5,5,24)")
                    db.execSQL("INSERT INTO StockData VALUES(86,5,6,24)")
                    db.execSQL("INSERT INTO StockData VALUES(87,5,7,34)")
                    db.execSQL("INSERT INTO StockData VALUES(88,5,8,24)")
                    db.execSQL("INSERT INTO StockData VALUES(89,5,9,14)")
                    db.execSQL("INSERT INTO StockData VALUES(90,5,10,14)")
                    db.execSQL("INSERT INTO StockData VALUES(91,5,11,14)")
                    db.execSQL("INSERT INTO StockData VALUES(92,5,12,11)")
                    db.execSQL("INSERT INTO StockData VALUES(93,5,13,4)")
                    db.execSQL("INSERT INTO StockData VALUES(94,5,14,6)")
                    db.execSQL("INSERT INTO StockData VALUES(95,5,15,15)")
                    db.execSQL("INSERT INTO StockData VALUES(96,5,16,9)")
                    db.execSQL("INSERT INTO StockData VALUES(97,5,17,14)")
                    db.execSQL("INSERT INTO StockData VALUES(98,5,18,24)")
                    db.execSQL("INSERT INTO StockData VALUES(99,5,19,14)")
                    db.execSQL("INSERT INTO StockData VALUES(100,5,20,7)")

                    db.execSQL("INSERT INTO StockData VALUES(101,6,1,34)")
                    db.execSQL("INSERT INTO StockData VALUES(102,6,2,24)")
                    db.execSQL("INSERT INTO StockData VALUES(103,6,3,24)")
                    db.execSQL("INSERT INTO StockData VALUES(104,6,4,24)")
                    db.execSQL("INSERT INTO StockData VALUES(105,6,5,24)")
                    db.execSQL("INSERT INTO StockData VALUES(106,6,6,24)")
                    db.execSQL("INSERT INTO StockData VALUES(107,6,7,34)")
                    db.execSQL("INSERT INTO StockData VALUES(108,6,8,24)")
                    db.execSQL("INSERT INTO StockData VALUES(109,6,9,14)")
                    db.execSQL("INSERT INTO StockData VALUES(110,6,10,14)")
                    db.execSQL("INSERT INTO StockData VALUES(111,6,11,14)")
                    db.execSQL("INSERT INTO StockData VALUES(112,6,12,11)")
                    db.execSQL("INSERT INTO StockData VALUES(113,6,13,4)")
                    db.execSQL("INSERT INTO StockData VALUES(114,6,14,6)")
                    db.execSQL("INSERT INTO StockData VALUES(115,6,15,15)")
                    db.execSQL("INSERT INTO StockData VALUES(116,6,16,9)")
                    db.execSQL("INSERT INTO StockData VALUES(117,6,17,14)")
                    db.execSQL("INSERT INTO StockData VALUES(118,6,18,24)")
                    db.execSQL("INSERT INTO StockData VALUES(119,6,19,14)")
                    db.execSQL("INSERT INTO StockData VALUES(120,6,20,7)")
                }
            })
            //.openHelperFactory(supportFactory)
            .fallbackToDestructiveMigration()
            .build()
        database = new
        return new
    }
}