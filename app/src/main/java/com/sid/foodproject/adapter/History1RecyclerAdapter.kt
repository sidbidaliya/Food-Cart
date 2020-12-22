package com.sid.foodproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sid.foodproject.R
import com.sid.foodproject.model.Menu

class History1RecyclerAdapter(val context: Context, private val foodList: ArrayList<Menu>) : RecyclerView.Adapter<History1RecyclerAdapter.History1ViewHolder>() {
    class History1ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dishName : TextView = view.findViewById(R.id.txtDishName2)
        val price : TextView = view.findViewById(R.id.txtPrice2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): History1ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_history_1_single_row, parent, false)
        return History1ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: History1ViewHolder, position: Int) {
        val menu = foodList[position]
        holder.dishName.text = menu.DishName
        holder.price.text = menu.DishPrice
    }
}