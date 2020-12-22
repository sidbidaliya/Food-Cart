package com.sid.foodproject.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.room.Room
import com.google.android.material.navigation.NavigationView
import com.sid.foodproject.R
import com.sid.foodproject.fragments.*
import com.sid.foodproject.restaurantDatabase.RestaurantDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    var previousMenuItem: MenuItem? = null
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)

        val navHeaderView= navigationView.getHeaderView(0)

        val txtUserName = navHeaderView.findViewById<TextView>(R.id.txtUserName)
        txtUserName.text = sharedPreferences.getString("name","Name")
        val txtContact = navHeaderView.findViewById<TextView>(R.id.txtUserNo)
        txtContact.text = sharedPreferences.getString("mobile_number","Contact Number")

        navHeaderView.setOnClickListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            val fragment = ProfileFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment)
            transaction.commit()
            supportActionBar?.title = "Profile"
            navigationView.setCheckedItem(R.id.home)
            drawerLayout.closeDrawer(GravityCompat.START)
            navigationView.setCheckedItem(R.id.myProfile)
            previousMenuItem = navigationView.checkedItem
        }

        setUpToolbar()
        openHome()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {
            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it
            when (it.itemId) {
                R.id.home -> {
                    openHome()
                    drawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.myProfile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ProfileFragment()
                        )
                        .commit()
                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.favRest -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FavouriteFragment()
                        )
                        .commit()
                    supportActionBar?.title = "Favourite Restaurants"
                    drawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.orderHistory -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            HistoryFragment()
                        )
                        .commit()
                    supportActionBar?.title = "Order History"
                    drawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.faq -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FaqFragment()
                        )
                        .commit()
                    supportActionBar?.title = "Frequently Asked Questions"
                    drawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.logOut -> {
                    val intent = Intent(this@MainActivity,LoginActivity::class.java)
                    val dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Do You Want to Logout?")
                    dialog.setPositiveButton("Yes"){
                            text,listener ->
                        sharedPreferences.edit().clear().apply()
                        DeleteOrders(this).execute().get()
                        startActivity(intent)
                        finish()
                    }
                    dialog.setNegativeButton("No"){
                            text,listener -> // Do Nothing
                        openHome()
                    }
                    dialog.create()
                    dialog.show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun openHome() {
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else if (frag !is HomeFragment) {
            openHome()
        } else {
            finish()
            super.onBackPressed()
        }
    }

    class DeleteOrders(val context: Context) : AsyncTask<Void, Void, Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, RestaurantDatabase::class.java,"res-db").build()
            db.resDao().deleteAll()
            return true
        }
    }
}
