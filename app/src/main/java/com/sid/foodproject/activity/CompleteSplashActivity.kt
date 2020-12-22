package com.sid.foodproject.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.room.Room
import com.sid.foodproject.R
import com.sid.foodproject.cartDatabase.CartDatabase

class CompleteSplashActivity : AppCompatActivity() {

    lateinit var btnOk : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_splash)

        btnOk = findViewById(R.id.btnOk)

        btnOk.setOnClickListener {
            if (DeleteOrders(this).execute().get()){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    class DeleteOrders(val context: Context) : AsyncTask<Void, Void, Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, CartDatabase::class.java,"cart-db").build()
            db.cartDao().deleteAll()
            return true
        }
    }
}