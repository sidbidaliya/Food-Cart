package com.sid.foodproject.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
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
import com.sid.foodproject.adapter.HomeRecyclerAdapter
import com.sid.foodproject.model.Restaurant
import com.sid.foodproject.util.ConnectionManager
import org.json.JSONException
import java.util.*

class HomeFragment : Fragment() {

    lateinit var recyclerHome: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    val resInfoList: ArrayList<Restaurant> = arrayListOf()
    private val sortOptions =
        arrayOf<CharSequence>("Cost (Low to High)", "Cost (High to Low)", "Rating")
    private var checkedItem = -1

    private var resComparator = Comparator<Restaurant> { res1, res2 ->
        if (res1.ResCfo.compareTo(res2.ResCfo, true) == 0) {
            //Sort according to name if rating is same
            res1.ResName.compareTo(res2.ResName, true)
        } else {
            res1.ResCfo.compareTo(res2.ResCfo, true)
        }
    }

    private var ratingComparator = Comparator<Restaurant> { res1, res2 ->
        if (res1.ResRating.compareTo(res2.ResRating, true) == 0) {
            //Sort according to name if rating is same
            res1.ResName.compareTo(res2.ResName, true)
        } else {
            res1.ResRating.compareTo(res2.ResRating, true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)

        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        recyclerHome = view.findViewById(R.id.recyclerHome)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    //Here we will Handle Response
                    try {
                        progressLayout.visibility = View.GONE
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {

                            val resArray = data.getJSONArray("data")
                            for (i in 0 until resArray.length()) {
                                val resObject = resArray.getJSONObject(i)
                                val restaurant = Restaurant(
                                    resObject.getString("id"),
                                    resObject.getString("name"),
                                    resObject.getString("rating"),
                                    resObject.getString("cost_for_one"),
                                    resObject.getString("image_url")
                                )
                                resInfoList.add(restaurant)
                                if (activity != null) {
                                    recyclerAdapter =
                                        HomeRecyclerAdapter(context as Context, resInfoList)
                                    recyclerHome.adapter = recyclerAdapter
                                    recyclerHome.layoutManager = LinearLayoutManager(activity)
                                }
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

    @SuppressLint("ResourceType")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menuSort -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("Sort by?")
                builder.setSingleChoiceItems(
                    sortOptions, checkedItem
                ) { dialog, which ->
                    // user checked an item
                    when (which) {
                        0 -> {
                            Collections.sort(resInfoList, resComparator)
                            checkedItem = 0
                        }
                        1 -> {
                            Collections.sort(resInfoList, resComparator)
                            resInfoList.reverse()
                            checkedItem = 1
                        }
                        2 -> {
                            Collections.sort(resInfoList, ratingComparator)
                            resInfoList.reverse()
                            checkedItem = 2
                        }
                    }
                }

                builder.setNegativeButton("Cancel") { dialogInterface, i ->
                    //do nothing
                }
                    .setPositiveButton("Ok") { dialogInterface, i ->
                        recyclerAdapter.notifyDataSetChanged()
                        checkedItem = -1
                    }

                builder.create().show()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
