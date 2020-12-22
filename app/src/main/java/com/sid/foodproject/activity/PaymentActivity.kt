package com.sid.foodproject.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.sid.foodproject.adapter.PaymentRecyclerAdapter
import com.sid.foodproject.cartDatabase.CartDatabase
import com.sid.foodproject.cartDatabase.CartEntity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class PaymentActivity : AppCompatActivity() {

    lateinit var recyclerCart: RecyclerView
    lateinit var recyclerAdapter: PaymentRecyclerAdapter
    lateinit var btnPlace: Button
    private var sum: Int = 0
    lateinit var sharedPreferences: SharedPreferences
    lateinit var toolbarCart : Toolbar
    lateinit var resName : TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        toolbarCart = findViewById(R.id.cartToolbar)

        setSupportActionBar(toolbarCart)
        supportActionBar?.title = "My Cart"

        resName = findViewById(R.id.txtResName2)
        recyclerCart = findViewById(R.id.recyclerOrder)
        btnPlace = findViewById(R.id.btnPlace)

        resName.text = intent.getStringExtra("res_name")

        val list = GetOrders(this).execute().get()
        for (i in list.indices) {
            sum += list[i].dishCfo.toInt()
        }
        btnPlace.text = "Place order (Total: Rs.$sum)"

        recyclerAdapter =
            PaymentRecyclerAdapter(this, list)
        recyclerCart.adapter = recyclerAdapter
        recyclerCart.layoutManager = LinearLayoutManager(this)

        val userId = sharedPreferences.getString("user_id","userId")
        val resId = intent.getStringExtra("res_id")

        btnPlace.setOnClickListener {
            val queue = Volley.newRequestQueue(this@PaymentActivity)
            val url = "http://13.235.250.119/v2/place_order/fetch_result"
            val jsonParams = JSONObject()
            jsonParams.put("user_id",userId)
            jsonParams.put("restaurant_id",resId)
            jsonParams.put("total_cost", sum.toString())
            val arrayOrder = JSONArray()
            for (element in list){
                val jsonOrder = JSONObject()
                jsonOrder.put("food_item_id", element.dish_id)
                arrayOrder.put(jsonOrder)
            }
            jsonParams.put("food", arrayOrder)

            val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                //Responses
                println("Response is $it")
                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        val intent = Intent(this, CompleteSplashActivity::class.java)
                        startActivity(intent)
                        ActivityCompat.finishAffinity(this@PaymentActivity)
                    } else {
                        Toast.makeText(
                            this,
                            "Some Error Occurred!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(
                        this,
                        "Some unexpected Error Occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, Response.ErrorListener {
                //Errors
                Toast.makeText(this, "Volley Error  $it", Toast.LENGTH_SHORT).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["token"] = "d93927ade44514"
                    return headers
                }
            }
            queue.add(jsonRequest)
        }
    }

    class GetOrders(val context: Context) : AsyncTask<Void, Void, List<CartEntity>>() {
        override fun doInBackground(vararg params: Void?): List<CartEntity> {
            val db = Room.databaseBuilder(context, CartDatabase::class.java, "cart-db").build()
            return db.cartDao().getAllCart()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
