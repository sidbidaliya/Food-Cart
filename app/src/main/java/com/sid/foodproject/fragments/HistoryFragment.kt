package com.sid.foodproject.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sid.foodproject.R
import com.sid.foodproject.adapter.HistoryRecyclerAdapter
import com.sid.foodproject.model.History
import com.sid.foodproject.util.ConnectionManager
import org.json.JSONException

class HistoryFragment : Fragment() {

    lateinit var recyclerHistory: RecyclerView
    lateinit var recyclerAdapter: HistoryRecyclerAdapter
    val orderInfoList: ArrayList<History> = arrayListOf()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    lateinit var frameLayout: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_history, container, false)

        progressBar = view.findViewById(R.id.progressBarH)
        progressLayout = view.findViewById(R.id.progressLayoutH)
        progressLayout.visibility = View.VISIBLE
        frameLayout = view.findViewById(R.id.historyFrame)

        sharedPreferences = this.activity!!.getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )

        val userId = sharedPreferences.getString("user_id", "User ID")

        recyclerHistory = view.findViewById(R.id.recyclerHistory)

        val queue = Volley.newRequestQueue(activity as Context)
        println("Response is --> $userId")
        val url = "http://13.235.250.119/v2/orders/fetch_result/${userId}"
        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    //Here we will Handle Response
                    try {
                        println("Response is $it")
                        progressLayout.visibility = View.GONE
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val resArray = data.getJSONArray("data")

                            for (i in 0 until resArray.length()) {
                                val resObject = resArray.getJSONObject(i)

                                val foodArray = resObject.getJSONArray("food_items")

                                val history = History(
                                    resObject.getString("order_id"),
                                    resObject.getString("restaurant_name"),
                                    resObject.getString("total_cost"),
                                    resObject.getString("order_placed_at"),
                                    foodArray
                                )
                                orderInfoList.add(history)
                                    if (activity != null) {
                                        recyclerAdapter =
                                            HistoryRecyclerAdapter(activity as Context, orderInfoList)
                                        recyclerHistory.adapter = recyclerAdapter
                                        recyclerHistory.layoutManager = LinearLayoutManager(activity)
                                }
                            }
                            if (orderInfoList.isEmpty()){
                                frameLayout.removeAllViews()
                                view = layoutInflater.inflate(R.layout.empty_history, frameLayout, false)
                                frameLayout.addView(view)
                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Error Occurred!!!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some unexpected error occurred!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    //Here we will handle Error
                    if (activity != null)
                        Toast.makeText(
                            activity as Context,
                            "Volley error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "d93927ade44514"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        } else {
            //Internet is available
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }


}
