package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class TrackerActivity : AppCompatActivity() {

    private lateinit var edtSleep: EditText
    private lateinit var edtWater: EditText
    private lateinit var chkExercise: CheckBox
    private lateinit var btnSubmit: Button

    private val serverUrl = "http://10.0.2.2:5000/submit_tracker"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker)

        edtSleep = findViewById(R.id.edtSleepHours)
        edtWater = findViewById(R.id.edtWaterIntake)
        chkExercise = findViewById(R.id.chkExercise)
        btnSubmit = findViewById(R.id.btnSubmitTracker)

        btnSubmit.setOnClickListener {
            val sleepStr = edtSleep.text.toString().trim()
            val waterStr = edtWater.text.toString().trim()
            val exercised = if (chkExercise.isChecked) 1 else 0

            if (sleepStr.isEmpty() || waterStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sleepHours = sleepStr.toFloatOrNull()
            val waterIntake = waterStr.toFloatOrNull()

            if (sleepHours == null || waterIntake == null) {
                Toast.makeText(this, "Please enter valid numeric values.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val userEmail = sharedPref.getString("user_email", "") ?: ""

            if (userEmail.isEmpty()) {
                Toast.makeText(this, "Please login first.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val requestQueue = Volley.newRequestQueue(this)
            val stringRequest = object : StringRequest(
                Request.Method.POST, serverUrl,
                { response ->
                    if (response.trim() == "success") {
                        Toast.makeText(this, "Wellness data saved!", Toast.LENGTH_LONG).show()
                        edtSleep.text.clear()
                        edtWater.text.clear()
                        chkExercise.isChecked = false
                    } else {
                        Toast.makeText(this, "Failed to save data.", Toast.LENGTH_LONG).show()
                    }
                },
                { error ->
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    return hashMapOf(
                        "user_email" to userEmail,
                        "sleep_hours" to sleepHours.toString(),
                        "water_intake" to waterIntake.toString(),
                        "exercised" to exercised.toString()
                    )
                }
            }

            requestQueue.add(stringRequest)
        }
    }
}
