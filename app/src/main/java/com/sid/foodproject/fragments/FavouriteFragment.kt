package com.sid.foodproject.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.sid.foodproject.R
import com.sid.foodproject.adapter.FavouriteRecyclerAdapter
import com.sid.foodproject.restaurantDatabase.ResEntity
import com.sid.foodproject.restaurantDatabase.RestaurantDatabase
import kotlin.properties.Delegates

class FavouriteFragment : Fragment() {

    private lateinit var recyclerFavourite: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter
    var dbResList = listOf<ResEntity>()       //To store the restaurant list
    var layoutSelect by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layoutSelect = if (CheckDatabase(context as Context).execute().get() == 0){
            R.layout.empty_favourite
        }else{
            R.layout.fragment_favourite
        }
        val view = inflater.inflate(layoutSelect, container, false)

        if(layoutSelect == R.layout.fragment_favourite){

            progressLayout = view.findViewById(R.id.progressLayoutF)
            progressLayout.visibility = View.VISIBLE
            progressBar = view.findViewById(R.id.progressBarF)
            recyclerFavourite = view.findViewById(R.id.recyclerFavourite)

            dbResList = RetrieveFavourites(activity as Context).execute().get()

            if (activity != null) {
                progressLayout.visibility = View.GONE
                recyclerAdapter = FavouriteRecyclerAdapter(activity as Context,
                    dbResList as ArrayList<ResEntity>
                )
                recyclerFavourite.adapter = recyclerAdapter
                recyclerFavourite.layoutManager = LinearLayoutManager(activity)
            }
        }
        return view
    }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void, Void, List<ResEntity>>() {
        override fun doInBackground(vararg params: Void?): List<ResEntity> {
            val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "res-db")
                .build()        //Initialise the database
            return db.resDao().getAllRes()       //Return the list of restaurants
        }
    }

    class CheckDatabase(val context: Context) : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg params: Void?): Int {
            val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "res-db")
                .build()        //Initialise the database
            return db.resDao().checkRes() //Check for number of restaurants in database
        }
    }
}
