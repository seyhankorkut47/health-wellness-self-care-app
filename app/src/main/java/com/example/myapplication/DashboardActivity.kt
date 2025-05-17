package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        findViewById<Button>(R.id.btnScheduleAppointment).setOnClickListener {
            startActivity(Intent(this, AppointmentActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewResources).setOnClickListener {
            startActivity(Intent(this, ResourcesActivity::class.java))
        }

        findViewById<Button>(R.id.btnTrackWellness).setOnClickListener {
            startActivity(Intent(this, TrackerActivity::class.java))
        }

        findViewById<Button>(R.id.btnSetReminders).setOnClickListener {
            startActivity(Intent(this, RemindersActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewProgress).setOnClickListener {
            startActivity(Intent(this, ProgressActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewAppointments).setOnClickListener {
            startActivity(Intent(this, AppointmentsListActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewTrackerData).setOnClickListener {
            startActivity(Intent(this, TrackerDataActivity::class.java))
        }



        // Çıkış yap butonu
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove("user_email")
                apply()
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
