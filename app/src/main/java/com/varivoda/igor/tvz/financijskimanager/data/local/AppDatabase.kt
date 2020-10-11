package com.varivoda.igor.tvz.financijskimanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.dao.ProductDao
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product

@Database(entities = [Product::class],version = 1,exportSchema = false)
abstract class AppDatabase: RoomDatabase(){

    abstract val productDao: ProductDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"appDatabase.db")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}