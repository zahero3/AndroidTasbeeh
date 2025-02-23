package com.example.tasbeeh

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class SavedCountsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_counts)

        val savedCountsLayout: LinearLayout = findViewById(R.id.savedCountsLayout)
        val btnClose: Button = findViewById(R.id.btnClose)

        val sharedPreferences = getSharedPreferences("SavedCounts", Context.MODE_PRIVATE)
        val allEntries = sharedPreferences.all

        if (allEntries.isNotEmpty()) {
            for ((key, value) in allEntries) {
                val textView = TextView(this)
                val data = value.toString().split(" - ")
                val count = data[0]
                val timestamp = data[1].toLong()

                val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(timestamp))

                textView.text = "📌 $key: $count (Saved on: $formattedDate)"
                textView.textSize = 18f
                textView.setPadding(10, 10, 10, 10)

                savedCountsLayout.addView(textView)
            }
        } else {
            val textView = TextView(this)
            textView.text = "No saved counts yet!"
            textView.textSize = 20f
            textView.setPadding(20, 20, 20, 20)

            savedCountsLayout.addView(textView)
        }

        btnClose.setOnClickListener {
            finish()
        }
    }
}