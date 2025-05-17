package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class AppointmentActivity : AppCompatActivity() {

    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var radioGroupType: RadioGroup
    private lateinit var btnConfirm: Button


    private val serverUrl = "http://10.0.2.2:5000/insert_appointment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)


        datePicker = findViewById(R.id.datePicker)
        timePicker = findViewById(R.id.timePicker)
        radioGroupType = findViewById(R.id.radioGroupType)
        btnConfirm = findViewById(R.id.btnConfirmAppointment)


        btnConfirm.setOnClickListener {

            val day = datePicker.dayOfMonth
            val month = datePicker.month + 1
            val year = datePicker.year
            val date = "$year-${"%02d".format(month)}-${"%02d".format(day)}"

            val hour = timePicker.hour
            val minute = timePicker.minute
            val time = String.format("%02d:%02d:00", hour, minute)

            val selectedTypeId = radioGroupType.checkedRadioButtonId
            if (selectedTypeId == -1) {
                Toast.makeText(this, "Please select appointment type.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val selectedRadioButton = findViewById<RadioButton>(selectedTypeId)
            val appointmentType = selectedRadioButton.text.toString()


            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val userEmail = sharedPref.getString("user_email", "") ?: ""


            if (userEmail.isEmpty()) {
                Toast.makeText(this, "Please login first.", Toast.LENGTH_LONG).show()
                finish() // Gerekirse bu Activity'yi kapat
                return@setOnClickListener
            }


            val stringRequest = object : StringRequest(
                Request.Method.POST, serverUrl,
                Response.Listener { response ->
                    if (response.trim() == "success") {
                        Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Failed to book appointment.", Toast.LENGTH_LONG).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["user_email"] = userEmail
                    params["date"] = date
                    params["time"] = time
                    params["type"] = appointmentType
                    return params
                }
            }

            Volley.newRequestQueue(this).add(stringRequest)
        }
    }
}
