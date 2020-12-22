package com.sid.foodproject.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
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

class SignUpActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etEMail: EditText
    lateinit var etContactNo: EditText
    lateinit var etAddress: EditText
    lateinit var etPassWord: EditText
    lateinit var etConfirmPass: EditText
    lateinit var btnRegister: Button

    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etName = findViewById(R.id.etName)
        etEMail = findViewById(R.id.etEMail)
        etContactNo = findViewById(R.id.etContactNo)
        etAddress = findViewById(R.id.etAddress)
        etPassWord = findViewById(R.id.etPassWord)
        etConfirmPass = findViewById(R.id.etConfirmPass)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val mail = etEMail.text.toString()
            val name = etName.text.toString()
            val number = etContactNo.text.toString()
            val address = etAddress.text.toString()
            val password = etPassWord.text.toString()
            if (name.isEmpty()) {
                etName.error = "Enter Name"
                etName.requestFocus()
                return@setOnClickListener
            }
            if (name.length <= 2){
                etName.error = "Enter Valid Name"
                etName.requestFocus()
                return@setOnClickListener
            }
            if ((mail.isEmpty())) {
                etEMail.error = "Enter Email"
                etEMail.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                etEMail.error = "Enter valid Email"
                etEMail.requestFocus()
                return@setOnClickListener
            }
            if (number.isEmpty()) {
                etContactNo.error = "Enter contact number"
                etContactNo.requestFocus()
                return@setOnClickListener
            }
            if (number.length != 10) {
                etContactNo.error = "Enter valid contact number"
                etContactNo.requestFocus()
                return@setOnClickListener
            }
            if (address.isEmpty()) {
                etAddress.error = "Enter valid address"
                etAddress.requestFocus()
                return@setOnClickListener
            }
            if ((password.isEmpty())) {
                etPassWord.error = "Enter password"
                etPassWord.requestFocus()
                return@setOnClickListener
            }
            if (password.length <= 5) {
                etPassWord.error = "Min. size 6"
                etPassWord.requestFocus()
                return@setOnClickListener
            }
            if (etConfirmPass.text.toString().isEmpty()) {
                etConfirmPass.error = "confirm password"
                etConfirmPass.requestFocus()
                return@setOnClickListener
            }
            if (etConfirmPass.text.toString() != password) {
                etConfirmPass.error = "Password not matched"
                etConfirmPass.requestFocus()
                return@setOnClickListener
            }
            btnRegister.isEnabled = false
            btnRegister.visibility = View.INVISIBLE
            registerUser()
        }

    }

    private fun registerUser() {

        val queue = Volley.newRequestQueue(this@SignUpActivity)
        val url = "http://13.235.250.119/v2/register/fetch_result"

        val jsonParams = JSONObject()
        jsonParams.put("name", etName.text.toString())
        jsonParams.put("mobile_number", etContactNo.text.toString())
        jsonParams.put("password", etPassWord.text.toString())
        jsonParams.put("address", etAddress.text.toString())
        jsonParams.put("email", etEMail.text.toString())

        val jsonRequest =
            object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                //Responses
                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        val sMsg = data.getString("successMessage")
                        Toast.makeText(this,sMsg,Toast.LENGTH_SHORT).show()
                        sharedPreference.edit().putBoolean("isLoggedIn", true).apply()
                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                        startActivity(intent)
                        ActivityCompat.finishAffinity(this@SignUpActivity)
                    } else {
                        val eMsg = data.getString("errorMessage")
                        Toast.makeText(this@SignUpActivity, eMsg, Toast.LENGTH_SHORT).show()
                        btnRegister.isEnabled = true
                        btnRegister.visibility = View.VISIBLE
                    }
                } catch (e: JSONException) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Some unexpected Error Occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                    btnRegister.isEnabled = true
                    btnRegister.visibility = View.VISIBLE
                }
            }, Response.ErrorListener {
                //Errors
                Toast.makeText(this@SignUpActivity, "Volley Error  $it", Toast.LENGTH_SHORT).show()
                btnRegister.isEnabled = true
                btnRegister.visibility = View.VISIBLE
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
