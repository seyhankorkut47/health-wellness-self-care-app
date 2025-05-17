package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    private val registerUrl = "http://10.0.2.2:5000/register_user"
    private val loginUrl = "http://10.0.2.2:5000/login_user"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Kayıtlı kullanıcı kontrolü
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val savedEmail = sharedPref.getString("user_email", null)
        if (!savedEmail.isNullOrEmpty()) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val editEmail = findViewById<EditText>(R.id.editTextEmail)
        val editPassword = findViewById<EditText>(R.id.editTextPassword)
        val checkboxShowPassword = findViewById<CheckBox>(R.id.checkboxShowPassword)

        // Şifreyi göster/gizle
        checkboxShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                editPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            editPassword.setSelection(editPassword.text.length)
        }

        btnLogin.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val requestQueue = Volley.newRequestQueue(this)
                val stringRequest = object : StringRequest(
                    Request.Method.POST, loginUrl,
                    { response ->
                        if (response.trim() == "success") {
                            with(sharedPref.edit()) {
                                putString("user_email", email)
                                apply()
                            }
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, DashboardActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { error ->
                        Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        return hashMapOf("email" to email, "password" to password)
                    }
                }
                requestQueue.add(stringRequest)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val requestQueue = Volley.newRequestQueue(this)
                val stringRequest = object : StringRequest(
                    Request.Method.POST, registerUrl,
                    { response ->
                        when (response.trim()) {
                            "success" -> {
                                with(sharedPref.edit()) {
                                    putString("user_email", email)
                                    apply()
                                }
                                Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, DashboardActivity::class.java))
                                finish()
                            }
                            "exists" -> Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { error ->
                        Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getParams(): MutableMap<String, String> {
                        return hashMapOf("email" to email, "password" to password)
                    }
                }
                requestQueue.add(stringRequest)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
