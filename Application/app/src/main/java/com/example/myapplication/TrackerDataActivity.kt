package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class TrackerDataActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private val url = "http://10.0.2.2:5000/get_tracker_data"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker_data)

        listView = findViewById(R.id.listViewTracker)

        val email = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("user_email", "") ?: ""

        val request = object : StringRequest(Method.GET, "$url?user_email=$email",
            { response ->
                val listItems = ArrayList<String>()
                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val sleep = obj.getString("sleep_hours")
                    val water = obj.getString("water_intake")
                    val exercised = obj.getBoolean("exercised")
                    listItems.add("Sleep: $sleep hrs, Water: $water L, Exercised: $exercised")
                }
                listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {}

        Volley.newRequestQueue(this).add(request)
    }
}
