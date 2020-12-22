package com.sid.foodproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sid.foodproject.R
import org.json.JSONException
import org.json.JSONObject

class GenerateOtpActivity : AppCompatActivity() {

    lateinit var etOtp : EditText
    lateinit var  etNewPass : EditText
    lateinit var  etConPass : EditText
    lateinit var  btnSubmit : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_otp)

        etOtp = findViewById(R.id.etOtp)
        etNewPass = findViewById(R.id.etNewPass)
        etConPass = findViewById(R.id.etConPass)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val otp = etOtp.text.toString()
            val new = etNewPass.text.toString()
            val cNew = etConPass.text.toString()
            if (otp.isEmpty()){
                etOtp.error = "Enter OTP"
                etOtp.requestFocus()
                return@setOnClickListener
            }
            if (otp.length != 4){
                etOtp.error = "Enter correct OTP"
                etOtp.requestFocus()
                return@setOnClickListener
            }
            if (new.isEmpty()){
                etNewPass.error = "Enter password"
                etNewPass.requestFocus()
                return@setOnClickListener
            }
            if (new.length <= 5){
                etNewPass.error = "Min. size 6"
                etNewPass.requestFocus()
                return@setOnClickListener
            }
            if (cNew.isEmpty()){
                etConPass.error = "confirm password"
                etConPass.requestFocus()
                return@setOnClickListener
            }
            if (cNew != new){
                etConPass.error = "Password not matched"
                etConPass.requestFocus()
                return@setOnClickListener
            }
            btnSubmit.isEnabled = false
            btnSubmit.visibility = View.INVISIBLE
            checkOtp()
        }
    }

    private fun checkOtp() {
        val otp : String = etOtp.text.toString()
        val cNew : String = etConPass.text.toString()

        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/reset_password/fetch_result"

        val jsonParams = JSONObject()
        jsonParams.put("mobile_number",intent.getStringExtra("mobile_number"))
        jsonParams.put("password",cNew)
        jsonParams.put("otp",otp)

        val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener{
            //Responses
            try {
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if (success) {
                    val msg = data.getString("successMessage")
                    println("Response is otp length - ${cNew.length}")
                    Toast.makeText(this@GenerateOtpActivity,msg,Toast.LENGTH_SHORT).show()
                    val intent = Intent (this, LoginActivity::class.java)
                    startActivity(intent)
                    ActivityCompat.finishAffinity(this@GenerateOtpActivity)
                } else {
                    val eMsg = data.getString("errorMessage")
                    Toast.makeText(this@GenerateOtpActivity, eMsg, Toast.LENGTH_SHORT).show()
                    btnSubmit.isEnabled = true
                    btnSubmit.visibility = View.VISIBLE
                }
            }catch (e: JSONException) {
                Toast.makeText(
                    this,
                    "Some unexpected Error Occurred!",
                    Toast.LENGTH_SHORT
                ).show()
                btnSubmit.isEnabled = true
                btnSubmit.visibility = View.VISIBLE
            }
        }, Response.ErrorListener {
            //Errors
            Toast.makeText(this,"Volley Error  $it", Toast.LENGTH_SHORT).show()
            btnSubmit.isEnabled = true
            btnSubmit.visibility = View.VISIBLE
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
