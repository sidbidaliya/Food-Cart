package com.sid.foodproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sid.foodproject.R
import com.sid.foodproject.cartDatabase.CartEntity

class PaymentRecyclerAdapter(val context: Context, private val dishList :List<CartEntity>) : RecyclerView.Adapter<PaymentRecyclerAdapter.CartViewHolder>() {
    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dishName : TextView = view.findViewById(R.id.txtDishName1)
        val price : TextView = view.findViewById(R.id.txtPrice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_payment_single_row, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val menu = dishList[position]
        holder.dishName.text = menu.dishName
        holder.price.text = menu.dishCfo
    }
}