package com.sid.foodproject.cartDatabase

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room

class DBAsyncTaskCart(val context: Context, val cartEntity: CartEntity, val mode: Int) :
    AsyncTask<Void, Void, Boolean>() {
    /*
    Mode1 -> check DB if dish is added to cart or not
    Mode2 -> add the dish into cart
    Mode3 -> Remove the dish from cart
   *  */
    val dbc = Room.databaseBuilder(context, CartDatabase::class.java, "cart-db").build()

    override fun doInBackground(vararg params: Void?): Boolean {
        when (mode) {
            1 -> {
                // check DB if dish is favourite or not
                val cart: CartEntity? =
                    dbc.cartDao().getCartById(cartEntity.dish_id)
                dbc.close()
                return cart != null
            }
            2 -> {
                //Save the dish into DB as favourite
                dbc.cartDao().insertCart(cartEntity)
                dbc.close()
                return true
            }
            3 -> {
                //Remove the dish from favourite
                dbc.cartDao().deleteCart(cartEntity)
                dbc.close()
                return true
            }

        }
        return false
    }
}