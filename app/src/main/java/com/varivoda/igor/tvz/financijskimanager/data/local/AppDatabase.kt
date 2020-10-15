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
                                db.execSQL("INSERT INTO County VALUES(20001,'Dubrovačka')")
                                db.execSQL("INSERT INTO County VALUES(20002,'Dubrovačka')")
                                db.execSQL("INSERT INTO County VALUES(20003,'Dubrovačka')")
                                db.execSQL("INSERT INTO County VALUES(20004,'Dubrovačka')")
                                db.execSQL("INSERT INTO County VALUES(20005,'hjk')")
                                db.execSQL("INSERT INTO County VALUES(20006,'jk')")
                                db.execSQL("INSERT INTO County VALUES(20007,'ghj')")
                                db.execSQL("INSERT INTO County VALUES(20008,'hjkh')")
                                db.execSQL("INSERT INTO County VALUES(20009,'Dubhrovačka')")
                                db.execSQL("INSERT INTO County VALUES(20010,'Dubjghrovačka')")
                                db.execSQL("INSERT INTO County VALUES(20020,'ghfg')")
                                db.execSQL("INSERT INTO County VALUES(20030,'gfd')")
                                db.execSQL("INSERT INTO County VALUES(20040,'sfd')")
                                db.execSQL("INSERT INTO Location VALUES(1,'Velika Gorica',10000)")
                                db.execSQL("INSERT INTO Store VALUES(1,'Žitnjak',null,1)")
                                db.execSQL("INSERT INTO Store VALUES(2,'Žitdasdnjak',null,1)")
                                db.execSQL("INSERT INTO Store VALUES(3,'asdasd',null,1)")
                                db.execSQL("INSERT INTO Store VALUES(4,'sd',null,1)")
                                db.execSQL("INSERT INTO Store VALUES(5,'asdasd',null,1)")
                                db.execSQL("INSERT INTO Store VALUES(6,'dad',null,1)")
                                db.execSQL("INSERT INTO Store VALUES(7,'asd',null,1)")
                                db.execSQL("INSERT INTO Store VALUES(8,'asdf',null,1)")
                                db.execSQL("INSERT INTO Store VALUES(9,'fdghd',null,1)")
                                db.execSQL("INSERT INTO Store VALUES(10,'gfhfg',null,1)")
                                db.execSQL("INSERT INTO Store VALUES(11,'cvb',null,1)")
                                db.execSQL("INSERT INTO Store VALUES(12,'gfhj',null,1)")
                                db.execSQL("INSERT INTO Store VALUES(13,'rzrt',null,1)")
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