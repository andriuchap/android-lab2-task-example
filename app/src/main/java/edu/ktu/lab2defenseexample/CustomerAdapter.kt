package edu.ktu.lab2defenseexample

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomerAdapter(
    private val data: MutableList<Customer>,
    private val itemClickListener: ItemClickListener,
    private val deleteClickedListener: DeleteClickListener
) : RecyclerView.Adapter<CustomerAdapter.ViewHolder>()
{
    interface ItemClickListener
    {
        fun onItemClick(position: Int)
    }

    interface DeleteClickListener
    {
        fun onDeleteClick(position: Int)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
    {
        var nameText = v.findViewById<TextView>(R.id.name_text)
        var surnameText = v.findViewById<TextView>(R.id.surname_text)
        var balanceText = v.findViewById<TextView>(R.id.balance_text)
        var deleteBtn = v.findViewById<ImageButton>(R.id.delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerAdapter.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_customer, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CustomerAdapter.ViewHolder, position: Int) {
        val customer = data!![position]

        holder.nameText.text = customer.firstName
        holder.surnameText.text = customer.surname
        holder.balanceText.text = "$${customer.balance.toString()}"
        if(customer.balance > 0.0)
        {
            holder.balanceText.setTextColor(Color.GREEN)
        }
        else
        {
            holder.balanceText.setTextColor(Color.RED)
        }

        holder.itemView.setOnClickListener(View.OnClickListener {
            itemClickListener.onItemClick(position)
        })
        holder.deleteBtn.setOnClickListener({
            deleteClickedListener.onDeleteClick(position)
        })
    }

    override fun getItemCount(): Int {
        return data!!.size
    }
}