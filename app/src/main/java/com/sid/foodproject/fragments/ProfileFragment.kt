package com.sid.foodproject.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.room.Room

import com.sid.foodproject.R
import com.sid.foodproject.activity.AddToCartActivity
import com.sid.foodproject.activity.LoginActivity
import com.sid.foodproject.cartDatabase.CartDatabase
import com.sid.foodproject.restaurantDatabase.RestaurantDatabase
import kotlinx.android.synthetic.main.activity_main.*

class ProfileFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtName : TextView
    lateinit var txtMail : TextView
    lateinit var txtContact : TextView
    lateinit var txtAddress : TextView
    lateinit var txtLogout : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences = this.activity!!.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        txtName = view.findViewById(R.id.userName)
        txtContact = view.findViewById(R.id.userPhone)
        txtMail = view.findViewById(R.id.userMail)
        txtAddress = view.findViewById(R.id.userAddress)
        txtLogout = view.findViewById(R.id.userLogout)

        txtName.text = sharedPreferences.getString("name","Name")
        txtMail.text = sharedPreferences.getString("email","Email@mail.com")
        txtContact.text = sharedPreferences.getString("mobile_number","1234567890")
        txtAddress.text = sharedPreferences.getString("address","Address")

        txtLogout.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            val dialog = AlertDialog.Builder(context as Context)
            dialog.setTitle("Confirmation")
            dialog.setMessage("Do You Want to Logout?")
            dialog.setPositiveButton("Yes"){
                    text,listener ->
                sharedPreferences.edit().clear().apply()
                DeleteOrders(context as Context).execute().get()
                startActivity(intent)
                activity?.finish()
            }
            dialog.setNegativeButton("No"){
                    text,listener -> // Do Nothing
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

    class DeleteOrders(val context: Context) : AsyncTask<Void, Void, Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, RestaurantDatabase::class.java,"res-db").build()
            db.resDao().deleteAll()
            return true
        }
    }

}
