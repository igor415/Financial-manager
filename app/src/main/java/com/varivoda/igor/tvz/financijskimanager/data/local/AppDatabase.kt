package com.varivoda.igor.tvz.financijskimanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.dao.CountyDao
import com.varivoda.igor.tvz.financijskimanager.data.local.dao.ProductDao
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.County
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Location
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store

@Database(entities = [Product::class,Location::class,County::class, Store::class],version = 5,exportSchema = false)
abstract class AppDatabase: RoomDatabase(){

    abstract val productDao: ProductDao
    abstract val countyDao: CountyDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"appDatabase.db")
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
                                db.execSQL("INSERT INTO Product VALUES(1,'SSD 120GB WD Green',246)")
                                db.execSQL("INSERT INTO Product VALUES(2,'SSD 256GB Gigabyte',349)")
                                db.execSQL("INSERT INTO Product VALUES(3,'SSD 240GB Kingston',381)")
                                db.execSQL("INSERT INTO Product VALUES(4,'Napajanje NaviaTech 400W',170)")
                                db.execSQL("INSERT INTO Product VALUES(5,'Napajanje Xilence 500W',251)")
                                db.execSQL("INSERT INTO Product VALUES(6,'Napajanje AKYGA 700W',299)")
                                db.execSQL("INSERT INTO Product VALUES(7,'Patriot Signature DDR4 2666Mhz 4GB',199)")
                                db.execSQL("INSERT INTO Product VALUES(8,'Kingston DDR4 2400MHz 8GB',369)")
                                db.execSQL("INSERT INTO Product VALUES(9,'Memorija G.Skill Aegis 8GB DDR4 3200MHz',349)")
                                db.execSQL("INSERT INTO Product VALUES(10,'Kingmax Zeus Gaming 16GB DDR4 3000MHz',466)")
                                db.execSQL("INSERT INTO Product VALUES(11,'Crucial 16GB DDR4 3200 MT/s',724)")
                                db.execSQL("INSERT INTO Product VALUES(12,'Procesor AMD Ryzen 3 1200',349)")
                                db.execSQL("INSERT INTO Product VALUES(13,'Procesor Intel Core i3 9100F',722)")
                                db.execSQL("INSERT INTO Product VALUES(14,'Procesor AMD Ryzen 5 1600 AF',799)")
                                db.execSQL("INSERT INTO Product VALUES(15,'Procesor Intel core i5 9400F',1249)")
                                db.execSQL("INSERT INTO Product VALUES(16,'Procesor AMD Ryzen Threadripper',1303)")
                                db.execSQL("INSERT INTO Product VALUES(17,'Procesor Intel core i5 9600K',2029)")
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
                                db.execSQL("INSERT INTO Customer VALUES(8,'Ana','Istuk',null,0,51000)")
                                db.execSQL("INSERT INTO BILL VALUES(1,1,1,5,'2020-06-01')")
                                db.execSQL("INSERT INTO BILL VALUES(2,4,2,4,'2020-06-01')")
                                db.execSQL("INSERT INTO BILL VALUES(3,7,3,3,'2020-06-01')")
                                db.execSQL("INSERT INTO BILL VALUES(4,10,4,2,'2020-06-01')")
                                db.execSQL("INSERT INTO BILL VALUES(5,16,5,8,'2020-06-01')")
                                db.execSQL("INSERT INTO BILL VALUES(6,13,6,7,'2020-06-01')")
                                db.execSQL("INSERT INTO ProductsOnBill VALUES(1,1,4,2)")
                                db.execSQL("INSERT INTO ProductsOnBill VALUES(2,2,1,2)")
                                db.execSQL("INSERT INTO ProductsOnBill VALUES(3,3,2,3)")
                                db.execSQL("INSERT INTO ProductsOnBill VALUES(4,4,7,1)")
                                db.execSQL("INSERT INTO ProductsOnBill VALUES(5,5,8,1)")
                                db.execSQL("INSERT INTO ProductsOnBill VALUES(6,6,9,3)")
                            }
                        })
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}