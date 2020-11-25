package edu.ktu.lab2defenseexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class CustomerActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val editMode = intent?.extras?.getBoolean("edit")

        val addBtn = findViewById<Button>(R.id.add_btn)
        addBtn.setOnClickListener(this)
        if(editMode != null && editMode)
        {
            addBtn.text = "Edit"
        }
        findViewById<Button>(R.id.cancel_btn).setOnClickListener(this)

        val name = intent?.extras?.getString("name")
        val surname = intent?.extras?.getString("surname")
        val balance = intent?.extras?.getFloat("balance")

        if(name!=null)
        {
            findViewById<EditText>(R.id.name_field).setText(name)
        }
        if(surname != null)
        {
            findViewById<EditText>(R.id.surname_field).setText(surname)
        }
        if(balance != null)
        {
            findViewById<EditText>(R.id.balance_field).setText(balance.toString())
        }
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.cancel_btn)
        {
            setResult(RESULT_CANCELED)
            finish()
        }
        else if(v?.id == R.id.add_btn)
        {
            val nameText = findViewById<EditText>(R.id.name_field)
            val surnameText = findViewById<EditText>(R.id.surname_field)
            val balance = findViewById<EditText>(R.id.balance_field)

            if(nameText.text.isEmpty() || surnameText.text.isEmpty() || balance.text.isEmpty())
            {
                Toast.makeText(this, "Make sure all fields are filled in!", Toast.LENGTH_LONG).show()
                return;
            }

            val intent = Intent()
            intent.putExtra("name", nameText.text.toString())
            intent.putExtra("surname", surnameText.text.toString())
            intent.putExtra("balance", balance.text.toString().toFloat())
            val pos = this.intent?.extras?.getInt("position", -1)
            if(pos!=-1)
            {
                intent.putExtra("position", pos)
            }

            setResult(RESULT_OK, intent)
            finish()
        }
    }
}