package edu.ktu.lab2defenseexample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Customer::class), version = 1)
abstract class AppDatabase() : RoomDatabase()
{
    abstract fun getCustomerDao() : CustomerDao

    companion object
    {
        private var instance : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase?
        {
            if(instance == null)
            {
                instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,
                    "database.db").build()
            }
            return instance
        }
    }
}