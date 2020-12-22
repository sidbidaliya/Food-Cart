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
import androidx.recyclerview.widget.RecyclerView
import com.sid.foodproject.R
import com.sid.foodproject.activity.AddToCartActivity
import com.sid.foodproject.restaurantDatabase.DBAsyncTask
import com.sid.foodproject.restaurantDatabase.ResEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context, private val favResList : ArrayList<ResEntity>) : RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {

    class FavouriteViewHolder(view: View): RecyclerView.ViewHolder(view){
        val favResName : TextView = view.findViewById(R.id.resName)
        val favResRating : TextView = view.findViewById(R.id.resRating)
        val favResCpp : TextView = view.findViewById(R.id.resCost)
        val favResImage : ImageView = view.findViewById(R.id.resImage)
        val llContentFav : LinearLayout = view.findViewById(R.id.llContentF)
        val addToFav : ImageView = view.findViewById(R.id.resLike)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row,parent,false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favResList.size
    }
    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val restaurant = favResList[position]
        holder.favResName.text = restaurant.resName
        holder.favResRating.text = restaurant.resRating
        holder.favResCpp.text = restaurant.resCfo
        Picasso.get().load(restaurant.resImage).error(R.drawable.app_logo).into(holder.favResImage)


        val resEntity = ResEntity(
            restaurant.res_id,
            restaurant.resName,
            restaurant.resRating,
            restaurant.resCfo,
            restaurant.resImage
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

        holder.llContentFav.setOnClickListener {
            val intent = Intent(context, AddToCartActivity::class.java)
            intent.putExtra("res_id",restaurant.res_id)
            intent.putExtra("res_name",restaurant.resName)
            context.startActivity(intent)
        }

    }
}