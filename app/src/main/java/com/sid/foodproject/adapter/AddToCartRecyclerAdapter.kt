package com.sid.foodproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sid.foodproject.R
import com.sid.foodproject.cartDatabase.CartEntity
import com.sid.foodproject.cartDatabase.DBAsyncTaskCart
import com.sid.foodproject.model.Menu

class AddToCartRecyclerAdapter(val context: Context, private val menuList : ArrayList<Menu>,
                               private val listener : OnItemClickListener) : RecyclerView.Adapter<AddToCartRecyclerAdapter.OrderViewHolder>() {
    class OrderViewHolder(view: View): RecyclerView.ViewHolder(view){
        val dishName : TextView = view.findViewById(R.id.txtDishName)
        val dishCpp : TextView = view.findViewById(R.id.txtDishCpp)
        val add : Button = view.findViewById(R.id.btnAdd)
    }

    interface OnItemClickListener{
        fun onAddItemClick(foodItem : Menu)
        fun onRemoveItemClick(foodItem: Menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_order_single_row,parent,false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val menu = menuList[position]
        holder.dishName.text = menu.DishName
        holder.dishCpp.text = menu.DishPrice

        val cartEntity = CartEntity(
            menu.DishId,
            menu.DishName,
            menu.DishPrice
        )

        val checkFav = DBAsyncTaskCart(context, cartEntity, 1).execute()
        val isFav = checkFav.get()
        if (isFav) {
            holder.add.setBackgroundResource(R.drawable.basic_custom_button_2)
            holder.add.text = "Remove"
        } else {
            holder.add.setBackgroundResource(R.drawable.basic_custom_button)
            holder.add.text = "Add"
        }

        holder.add.setOnClickListener {
            if (!DBAsyncTaskCart(context, cartEntity, 1).execute().get()){
                listener.onAddItemClick(menu)
                val async =
                    DBAsyncTaskCart(context, cartEntity, 2).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                        context,
                        "Added To Cart",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.add.setBackgroundResource(R.drawable.basic_custom_button_2)
                    holder.add.text = "Remove"
                } else {
                    Toast.makeText(
                        context,
                        "Some Error Occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                listener.onRemoveItemClick(menu)
                val async =
                    DBAsyncTaskCart(context, cartEntity, 3).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                        context,
                        "Removed from Cart",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.add.setBackgroundResource(R.drawable.basic_custom_button)
                    holder.add.text = "Add"
                } else {
                    Toast.makeText(
                        context,
                        "Some Error Occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}