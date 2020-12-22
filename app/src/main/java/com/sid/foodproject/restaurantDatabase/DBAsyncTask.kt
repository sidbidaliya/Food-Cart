package com.sid.foodproject.restaurantDatabase

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room

class DBAsyncTask(val context: Context, private val resEntity: ResEntity, private val mode: Int) :
    AsyncTask<Void, Void, Boolean>() {
    /*
    Mode1 -> check DB if res is favourite or not
    Mode2 -> Save the res into DB as favourite
    Mode3 -> Remove the res from favourite
   *  */
    private val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "res-db").build()

    override fun doInBackground(vararg params: Void?): Boolean {
        when (mode) {
            1 -> {
                // check DB if res is favourite or not
                val res: ResEntity? =
                    db.resDao().getResById(resEntity.res_id)
                db.close()
                return res != null
            }
            2 -> {
                //Save the res into DB as favourite
                db.resDao().insertRes(resEntity)
                db.close()
                return true
            }
            3 -> {
                //Remove the res from favourite
                db.resDao().deleteRes(resEntity)
                db.close()
                return true
            }

        }
        return false
    }
}