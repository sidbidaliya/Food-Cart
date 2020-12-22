package com.sid.foodproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sid.foodproject.R
import org.json.JSONException
import org.json.JSONObject

class ForgotPassActivity : AppCompatActivity() {

    lateinit var etContactNum: EditText
    lateinit var etMail: EditText
    lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        etContactNum = findViewById(R.id.etContactNum)
        etMail = findViewById(R.id.etMail)
        btnNext = findViewById(R.id.btnNext)

        btnNext.setOnClickListener {
            val num = etContactNum.text.toString()
            val mail = etMail.text.toString()
            if (num.isEmpty()){
                etContactNum.error = "Enter number"
                etContactNum.requestFocus()
                return@setOnClickListener
            }
            if (num.length != 10){
                etContactNum.error = "Enter valid number"
                etContactNum.requestFocus()
                return@setOnClickListener
            }
            if(mail.isEmpty()){
                etMail.error = "Enter Email"
                etMail.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                etMail.error = "Enter valid Email"
                etMail.requestFocus()
                return@setOnClickListener
            }
            btnNext.isEnabled = false
            btnNext.visibility = View.INVISIBLE
            requestOtp()
        }
    }

    private fun requestOtp() {
        val number : String = etContactNum.text.toString()
        val mail : String = etMail.text.toString()

        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

        val jsonParams = JSONObject()
        jsonParams.put("mobile_number",number)
        jsonParams.put("email",mail)

        val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener{
            //Responses
            try {
                println("Response is $it")
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if (success) {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("Information")
                    dialog.setMessage("Please refer to the previous email for the OTP")
                    dialog.setCancelable(false)
                    dialog.setPositiveButton("Ok") { text, listener ->
                        val intent = Intent(this,GenerateOtpActivity::class.java)
                        intent.putExtra("mobile_number", number)
                        startActivity(intent)
                        finish()
                    }
                    dialog.create()
                    dialog.show()
                } else {
                    val eMsg = data.getString("errorMessage")
                    Toast.makeText(this@ForgotPassActivity, eMsg, Toast.LENGTH_SHORT).show()
                    btnNext.isEnabled = true
                    btnNext.visibility = View.VISIBLE
                }
            }catch (e: JSONException) {
                Toast.makeText(
                    this,
                    "Some unexpected Error Occurred!",
                    Toast.LENGTH_SHORT
                ).show()
                btnNext.isEnabled = true
                btnNext.visibility = View.VISIBLE
            }
        }, Response.ErrorListener {
            //Errors
            Toast.makeText(this,"Volley Error  $it", Toast.LENGTH_SHORT).show()
            btnNext.isEnabled = true
            btnNext.visibility = View.VISIBLE
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
