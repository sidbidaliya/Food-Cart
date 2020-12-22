package com.sid.foodproject.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sid.foodproject.R
import com.sid.foodproject.adapter.AddToCartRecyclerAdapter
import com.sid.foodproject.cartDatabase.CartDatabase
import com.sid.foodproject.model.Menu
import com.sid.foodproject.util.ConnectionManager
import org.json.JSONException

class AddToCartActivity : AppCompatActivity() {

    lateinit var recyclerDetail: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var toolbarOrder : Toolbar
    lateinit var recyclerAdapter: AddToCartRecyclerAdapter
    lateinit var btnCart : Button

    val menuList: ArrayList<Menu> = arrayListOf()
    val orderList : ArrayList<Menu> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_cart)

        orderList.clear()

        toolbarOrder = findViewById(R.id.orderToolbar)

        val restaurantId = intent.getStringExtra("res_id")
        val resName = intent.getStringExtra("res_name")

        setSupportActionBar(toolbarOrder)
        supportActionBar?.title = resName

        btnCart = findViewById(R.id.btnCart)
        btnCart.visibility = View.GONE

        progressBar = findViewById(R.id.progressBarAtc)
        progressLayout = findViewById(R.id.progressLayoutAtc)

        progressLayout.visibility = View.VISIBLE

        btnCart.setOnClickListener {
            if (orderList.size > 0){
                val intent = Intent(this,PaymentActivity::class.java)
                intent.putExtra("res_id",restaurantId)
                intent.putExtra("res_name",resName)
                startActivity(intent)
            }else{
                Toast.makeText(this,"Select at least one item", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerDetail = findViewById(R.id.recyclerDetail)

        val resId = intent.getStringExtra("res_id")

        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/${resId}"

        if (ConnectionManager().checkConnectivity(this)) {

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
                                val menu = Menu(
                                    resObject.getString("id"),
                                    resObject.getString("name"),
                                    resObject.getString("cost_for_one")
                                )
                                val listener = object : AddToCartRecyclerAdapter.OnItemClickListener{
                                    override fun onAddItemClick(foodItem: Menu) {
                                        orderList.add(foodItem)
                                        if (orderList.size > 0){
                                            btnCart.visibility = View.VISIBLE
                                        }
                                    }

                                    override fun onRemoveItemClick(foodItem: Menu) {
                                        orderList.remove(foodItem)
                                        if (orderList.isEmpty()){
                                            btnCart.visibility = View.GONE
                                        }
                                    }
                                }
                                menuList.add(menu)
                                recyclerAdapter =
                                    AddToCartRecyclerAdapter(this, menuList,listener)
                                recyclerDetail.adapter = recyclerAdapter
                                recyclerDetail.layoutManager = LinearLayoutManager(this)

                            }
                        } else {
                            Toast.makeText(
                                    this,
                                    "Error Occurred!!!",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this,
                            "Some unexpected error occurred!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    //Here we will handle Error
                    Toast.makeText(
                        this,
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
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
        }

    }

    class DeleteOrders(val context: Context) : AsyncTask<Void, Void, Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, CartDatabase::class.java,"cart-db").build()
            db.cartDao().deleteAll()
            return true
        }
    }

    class CheckDatabase(val context: Context) : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg params: Void?): Int {
            val db = Room.databaseBuilder(context, CartDatabase::class.java, "cart-db")
                .build()        //Initialise the database
            return db.cartDao().checkItem() //Check for number of items in database
        }

    }

    override fun onBackPressed() {
        if(CheckDatabase(this@AddToCartActivity).execute().get() == 0){
            super.onBackPressed()
        }else{
            if (DeleteOrders(this@AddToCartActivity).execute().get()){
                super.onBackPressed()
            }else{
                Toast.makeText(this,"Some error occurred",Toast.LENGTH_SHORT).show()
            }
        }
    }

}
