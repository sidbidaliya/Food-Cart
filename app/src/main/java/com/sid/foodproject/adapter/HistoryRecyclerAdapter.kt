package com.sid.foodproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sid.foodproject.R
import com.sid.foodproject.model.History
import com.sid.foodproject.model.Menu
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HistoryRecyclerAdapter(val context: Context, private val historyList : ArrayList<History>) : RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resName : TextView = view.findViewById(R.id.txtResName1)
        val date : TextView = view.findViewById(R.id.txtDate)
        val cost : TextView = view.findViewById(R.id.txtTotal)
        val recyclerHistory1 : RecyclerView = view.findViewById(R.id.recyclerHistory1)
        lateinit var recyclerAdapter: History1RecyclerAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_history_single_row, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = historyList[position]
        holder.resName.text = history.RestName
        val format = SimpleDateFormat("dd-MM-yy HH:mm:ss")
        val format1 = SimpleDateFormat("dd/MM/yy | hh:mm a")
        var date: Date? = null
        try {
            date = format.parse(history.TimeDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val convertedDate: String = format1.format(date)
        holder.date.text = convertedDate
        holder.cost.text = history.TotalCost

        val foodList : ArrayList<Menu> = arrayListOf()
        for (i in 0 until history.FoodItems.length()){
            val data = history.FoodItems.getJSONObject(i)
            foodList.add(
                Menu(
                    data.getString("food_item_id"),
                    data.getString("name"),
                    data.getString("cost")
                )
            )
        }
        holder.recyclerAdapter = History1RecyclerAdapter(context,foodList)
        holder.recyclerHistory1.adapter = holder.recyclerAdapter
        holder.recyclerHistory1.layoutManager = LinearLayoutManager(context)

    }
}