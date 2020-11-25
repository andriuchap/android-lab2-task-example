package edu.ktu.lab2defenseexample

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CustomerDao
{
    @Insert
    fun insert(customer: Customer) : Single<Long>

    @Delete
    fun delete(customer: Customer) : Completable

    @Query("SELECT * FROM customer")
    fun getAllCustomers() : Single<List<Customer>>

    @Query("SELECT * FROM customer WHERE firstName LIKE :name")
    fun getCustomersByName(name:String) : Single<List<Customer>>

    @Update
    fun update(customer: Customer) : Completable
}