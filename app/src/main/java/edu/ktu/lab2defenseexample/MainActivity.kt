package edu.ktu.lab2defenseexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(),
    CustomerAdapter.ItemClickListener,
    CustomerAdapter.DeleteClickListener,
    TextWatcher {

    private var ADD_ITEM_REQUEST_CODE = 1
    private var EDIT_ITEM_REQUEST_CODE = 2

    lateinit var listView: RecyclerView
    lateinit var adapter: CustomerAdapter

    var data = mutableListOf<Customer>()
    lateinit var database: AppDatabase
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = AppDatabase.getInstance(this)!!

        listView = findViewById<RecyclerView>(R.id.list_view)
        listView.layoutManager = LinearLayoutManager(this)

        adapter = CustomerAdapter(data, this, this)

        listView.adapter = adapter

        getCustomers()
        findViewById<Button>(R.id.add_item_btn).setOnClickListener(View.OnClickListener {
            onAddCustomerClicked(it)
        })
        findViewById<EditText>(R.id.filter_field).addTextChangedListener(this)
    }

    fun getCustomers()
    {
        disposable = database?.getCustomerDao()
            ?.getAllCustomers()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    data.clear()
                    if(!it.isNullOrEmpty())
                    {
                        data.addAll(it)
                    }
                    adapter.notifyDataSetChanged()
                    disposable = null
                },
                {
                    Toast.makeText(this, "Error retrieving customers!", Toast.LENGTH_LONG).show()
                }
            )
    }

    fun onAddCustomerClicked(v: View)
    {
        val intent = Intent(this, CustomerActivity::class.java)
        startActivityForResult(intent, ADD_ITEM_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK)
        {
            val name = data?.extras?.getString("name")
            val surname = data?.extras?.getString("surname")
            val balance = data?.extras?.getFloat("balance")
            if(requestCode == ADD_ITEM_REQUEST_CODE) {
                val customer = Customer(0, name, surname, balance!!)

                disposable = database
                    .getCustomerDao()
                    .insert(customer)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            this.data.add(
                                Customer(
                                    it.toInt(),
                                    customer.firstName,
                                    customer.surname,
                                    customer.balance
                                )
                            )
                            adapter.notifyItemInserted(this.data.size)
                            disposable = null
                        },
                        {
                            Toast.makeText(this, "Failed to add new customer!", Toast.LENGTH_LONG)
                                .show()
                        }
                    )
            }
            if(requestCode == EDIT_ITEM_REQUEST_CODE)
            {
                val position = data?.extras?.getInt("position")
                val customer = Customer(this.data[position!!].uid, name, surname, balance!!)
                disposable = database
                    .getCustomerDao()
                    .update(customer)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            this.data[position] = customer
                            adapter.notifyItemChanged(position)
                            disposable = null
                        },
                        {
                            Toast.makeText(this, "Failed to update values!", Toast.LENGTH_LONG)
                        }
                    )
            }
        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, CustomerActivity::class.java)

        intent.putExtra("edit", true)
        intent.putExtra("position", position)
        //intent.putExtra("uid", data)
        intent.putExtra("name", data[position].firstName)
        intent.putExtra("surname", data[position].surname)
        intent.putExtra("balance", data[position].balance)

        startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE)
    }

    override fun onDeleteClick(position: Int) {
        disposable = database
            .getCustomerDao()
            .delete(data[position])
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    data.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    disposable = null
                },
                {
                    Toast.makeText(this, "Failed to delete customer!", Toast.LENGTH_LONG).show()
                }
            )
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        disposable?.dispose();

        if(findViewById<EditText>(R.id.filter_field).text.isEmpty())
        {
            getCustomers()
        }
        else {
            disposable = database.getCustomerDao()
                .getCustomersByName(p0.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        data.clear()
                        if (!it.isNullOrEmpty()) {
                            data.addAll(it)
                        }
                        adapter.notifyDataSetChanged()
                        disposable = null
                    },
                    {
                        Toast.makeText(this, "Error retrieving customers!", Toast.LENGTH_LONG)
                            .show()
                    }
                )
        }
    }
}