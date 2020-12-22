package com.sid.foodproject.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sid.foodproject.R
import com.sid.foodproject.activity.AddToCartActivity
import com.sid.foodproject.restaurantDatabase.DBAsyncTask
import com.sid.foodproject.restaurantDatabase.ResEntity
import com.sid.foodproject.model.Restaurant
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context: Context, private val resList : ArrayList<Restaurant>) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view){
        val resName : TextView = view.findViewById(R.id.resName)
        val resRating : TextView = view.findViewById(R.id.resRating)
        val resCpp : TextView = view.findViewById(R.id.resCost)
        val resImage : ImageView = view.findViewById(R.id.resImage)
        val llContent : LinearLayout = view.findViewById(R.id.llContentF)
        val addToFav : ImageView = view.findViewById(R.id.resLike)
        val cardView : CardView = view.findViewById(R.id.cardLL)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return resList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant = resList[position]
        holder.resName.text = restaurant.ResName
        holder.resRating.text = restaurant.ResRating
        holder.resCpp.text = restaurant.ResCfo + "/Person"
        Picasso.get().load(restaurant.ResPic).error(R.drawable.app_logo).into(holder.resImage)

        val resEntity = ResEntity(
            restaurant.ResId,
            restaurant.ResName,
            restaurant.ResRating,
            restaurant.ResCfo,
            restaurant.ResPic
        )

        val checkFav = DBAsyncTask(context, resEntity, 1).execute()
        val isFav = checkFav.get()
        if (isFav) {
            holder.addToFav.setBackgroundResource(R.drawable.ic_heart_fill_red)
        } else {
            holder.addToFav.setBackgroundResource(R.drawable.ic_heart_hollow)
        }

        holder.addToFav.setOnClickListener {
            if (!DBAsyncTask(context, resEntity, 1).execute().get()){
                val async =
                    DBAsyncTask(context, resEntity, 2).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                        context,
                        "Added To Favourites",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.addToFav.setBackgroundResource(R.drawable.ic_heart_fill_red)
                } else {
                    Toast.makeText(
                        context,
                        "Some Error Occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                val async =
                    DBAsyncTask(context, resEntity, 3).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                        context,
                        "Removed from Favourites",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.addToFav.setBackgroundResource(R.drawable.ic_heart_hollow)
                } else {
                    Toast.makeText(
                        context,
                        "Some Error Occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        holder.llContent.setOnClickListener {
            val intent = Intent(context,AddToCartActivity::class.java)
            intent.putExtra("res_id",restaurant.ResId)
            intent.putExtra("res_name",restaurant.ResName)
            context.startActivity(intent)
        }
    }
}