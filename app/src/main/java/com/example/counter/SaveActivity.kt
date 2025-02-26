package com.example.counter

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SaveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        val tvCurrentCount: TextView = findViewById(R.id.tvCurrentCount)
        val etCountName: EditText = findViewById(R.id.etCountName)
        val btnSubmit: Button = findViewById(R.id.btnSubmit)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        val currentCount = intent.getIntExtra("COUNT", 0)
        tvCurrentCount.text = "Current Count: $currentCount"

        val sharedPreferences = getSharedPreferences("SavedCounts", Context.MODE_PRIVATE)

        btnSubmit.setOnClickListener {
            val name = etCountName.text.toString()
            if (name.isNotEmpty()) {
                val editor = sharedPreferences.edit()
                editor.putString(name, "$currentCount - ${System.currentTimeMillis()}")
                editor.apply()
                Toast.makeText(this, "Count Saved!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Enter a name!", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}