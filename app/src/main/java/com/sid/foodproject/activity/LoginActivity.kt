package com.sid.foodproject.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sid.foodproject.R
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var etMobileNum: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtForgotPassword : TextView
    private lateinit var txtRegister : TextView

    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreference =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreference.getBoolean("isLoggedIn", false)

        setContentView(R.layout.activity_login)

        if (isLoggedIn) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        title = "Log In"

        etMobileNum = findViewById(R.id.etMobileNo)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgotPassword = findViewById(R.id.txtForgotPass)
        txtRegister = findViewById(R.id.txtSignUp)

        txtForgotPassword.setOnClickListener {
            val intent1 = Intent(this@LoginActivity,ForgotPassActivity::class.java)
            startActivity(intent1)
        }
        txtRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity,SignUpActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            if (etMobileNum.text.toString().isEmpty()){
                etMobileNum.error = "Enter Mobile Number"
                etMobileNum.requestFocus()
                return@setOnClickListener
            }
            if (etMobileNum.text.toString().length != 10){
                etMobileNum.error = "Enter valid Mobile Number"
                etMobileNum.requestFocus()
                return@setOnClickListener
            }
            if (etPassword.text.toString().isEmpty()){
                etPassword.error = "Enter Password"
                etPassword.requestFocus()
                return@setOnClickListener
            }
            btnLogin.isEnabled = false
            btnLogin.visibility = View.INVISIBLE
            checkDetails()
        }
    }

    private fun checkDetails(){
        val username : String = etMobileNum.text.toString()
        val password : String = etPassword.text.toString()

        val queue = Volley.newRequestQueue(this@LoginActivity)
        val url = "http://13.235.250.119/v2/login/fetch_result/"

        val jsonParams = JSONObject()
        jsonParams.put("mobile_number",username)
        jsonParams.put("password",password)

        val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener{
            //Responses
            println("Response is $it")
            try {
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if (success) {
                    val userJsonObject = data.getJSONObject("data")
                    val userId = userJsonObject.getString("user_id")
                    val name = userJsonObject.getString("name")
                    val mail = userJsonObject.getString("email")
                    val contact = userJsonObject.getString("mobile_number")
                    val address = userJsonObject.getString("address")
                    sharedPreference.edit().putBoolean("isLoggedIn",true).apply()
                    sharedPreference.edit().putString("user_id",userId).apply()
                    sharedPreference.edit().putString("name",name).apply()
                    sharedPreference.edit().putString("email",mail).apply()
                    sharedPreference.edit().putString("mobile_number",contact).apply()
                    sharedPreference.edit().putString("address",address).apply()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val eMsg = data.getString("errorMessage")
                    Toast.makeText(this@LoginActivity, eMsg, Toast.LENGTH_SHORT).show()
                    btnLogin.isEnabled = true
                    btnLogin.visibility = View.VISIBLE
                }
            }catch (e: JSONException) {
                Toast.makeText(
                    this@LoginActivity,
                    "Some unexpected Error Occurred!",
                    Toast.LENGTH_SHORT
                ).show()
                btnLogin.isEnabled = true
                btnLogin.visibility = View.VISIBLE
            }
        }, Response.ErrorListener {
            //Errors
            Toast.makeText(this@LoginActivity,"Volley Error  $it", Toast.LENGTH_SHORT).show()
            btnLogin.isEnabled = true
            btnLogin.visibility = View.VISIBLE
        }){
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
