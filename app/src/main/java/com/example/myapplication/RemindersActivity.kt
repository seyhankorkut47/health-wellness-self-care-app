package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class RemindersActivity : AppCompatActivity() {

    private val saveUrl = "http://10.0.2.2:5000/insert_reminders"
    private val getUrl = "http://10.0.2.2:5000/get_reminders"

    private lateinit var timeExercise: TimePicker
    private lateinit var switchExercise: Switch
    private lateinit var timeWater: TimePicker
    private lateinit var switchWater: Switch
    private lateinit var timeSleep: TimePicker
    private lateinit var switchSleep: Switch
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders)

        timeExercise = findViewById(R.id.timeExercise)
        switchExercise = findViewById(R.id.switchExercise)
        timeWater = findViewById(R.id.timeWater)
        switchWater = findViewById(R.id.switchWater)
        timeSleep = findViewById(R.id.timeSleep)
        switchSleep = findViewById(R.id.switchSleep)
        btnSave = findViewById(R.id.btnSaveReminders)

        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userEmail = sharedPref.getString("user_email", "") ?: ""

        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Please login first.", Toast.LENGTH_SHORT).show()
            return
        }

        // 🔄 Hatırlatıcıları yükle
        loadReminders(userEmail)

        // 💾 Kaydet butonu
        btnSave.setOnClickListener {
            val exTime = String.format("%02d:%02d", timeExercise.hour, timeExercise.minute)
            val waTime = String.format("%02d:%02d", timeWater.hour, timeWater.minute)
            val slTime = String.format("%02d:%02d", timeSleep.hour, timeSleep.minute)

            val exEnabled = if (switchExercise.isChecked) "1" else "0"
            val waEnabled = if (switchWater.isChecked) "1" else "0"
            val slEnabled = if (switchSleep.isChecked) "1" else "0"

            val request = object : StringRequest(
                Request.Method.POST, saveUrl,
                { response ->
                    if (response.trim() == "success") {
                        Toast.makeText(this, "Reminders saved!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to save reminders.", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    return hashMapOf(
                        "user_email" to userEmail,
                        "exercise_time" to exTime,
                        "exercise_enabled" to exEnabled,
                        "water_time" to waTime,
                        "water_enabled" to waEnabled,
                        "sleep_time" to slTime,
                        "sleep_enabled" to slEnabled
                    )
                }
            }

            Volley.newRequestQueue(this).add(request)
        }
    }

    private fun loadReminders(userEmail: String) {
        val request = StringRequest(
            Request.Method.GET,
            "$getUrl?email=$userEmail",
            { response ->
                try {
                    val json = JSONObject(response)
                    timeExercise.hour = json.getString("exercise_time").split(":")[0].toInt()
                    timeExercise.minute = json.getString("exercise_time").split(":")[1].toInt()
                    switchExercise.isChecked = json.getBoolean("exercise_enabled")

                    timeWater.hour = json.getString("water_time").split(":")[0].toInt()
                    timeWater.minute = json.getString("water_time").split(":")[1].toInt()
                    switchWater.isChecked = json.getBoolean("water_enabled")

                    timeSleep.hour = json.getString("sleep_time").split(":")[0].toInt()
                    timeSleep.minute = json.getString("sleep_time").split(":")[1].toInt()
                    switchSleep.isChecked = json.getBoolean("sleep_enabled")
                } catch (e: Exception) {
                    // Veritabanında kayıt yoksa sessiz geç
                }
            },
            { error ->
                Toast.makeText(this, "Could not load reminders", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}
