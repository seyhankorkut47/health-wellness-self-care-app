package com.example.myapplication

import android.os.Bundle
import android.widget.ListView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class AppointmentsListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private val url = "http://10.0.2.2:5000/get_appointments"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments_list)

        listView = findViewById(R.id.listViewAppointments)

        val email = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("user_email", "")

        if (email.isNullOrEmpty()) {
            Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show()
            return
        }

        val stringRequest = object : StringRequest(
            Method.GET,
            "$url?email=$email",
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    val listItems = ArrayList<String>()
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        listItems.add("${obj.getString("date")} ${obj.getString("time")} - ${obj.getString("type")}")
                    }
                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
                    listView.adapter = adapter
                } catch (e: Exception) {
                    Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {}

        Volley.newRequestQueue(this).add(stringRequest)
    }
}
