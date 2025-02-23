package com.example.tasbeeh

import android.content.Intent
import android.app.Service
import android.content.Context
import android.os.Vibrator
import android.os.VibrationEffect
import android.os.Build
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var count = 0
    private lateinit var tvCounter: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find views
        tvCounter = findViewById(R.id.tvCounter)
        val btnCount: Button = findViewById(R.id.btnCount)
        val btnReset: Button = findViewById(R.id.btnReset)
        val btnSave: Button = findViewById(R.id.btnSave)
        val btnSaved: Button = findViewById(R.id.btnSaved)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("CountPrefs", Context.MODE_PRIVATE)

        // Load saved count on startup
        count = sharedPreferences.getInt("last_count", 0)
        tvCounter.text = count.toString()

        // Count Button with Vibration
        btnCount.setOnClickListener {
            count++
            tvCounter.text = count.toString()
            /*vibrateDevice() // Vibrate on click*/
        }

        // Reset Button
        btnReset.setOnClickListener {
            count = 0
            tvCounter.text = "0"
            Toast.makeText(this, "Counter Reset", Toast.LENGTH_SHORT).show()
        }

        // Save Button
        btnSave.setOnClickListener {
            val intent = Intent(this, SaveActivity::class.java)
            intent.putExtra("COUNT", count)
            startActivity(intent)
        }

        // View Saved Counts
        btnSaved.setOnClickListener {
            val intent = Intent(this, SavedCountsActivity::class.java)
            startActivity(intent)
        }
        }
    }
/*
    // Vibrate function
    private fun vibrateDevice() {
        val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator?.hasVibrator() == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(100)
            }
        }
    }
*/