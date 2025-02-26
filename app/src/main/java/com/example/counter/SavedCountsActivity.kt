package com.example.counter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class SavedCountsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_counts)

        val savedCountsLayout: LinearLayout = findViewById(R.id.savedCountsLayout)
        val btnClose: Button = findViewById(R.id.btnClose)

        val sharedPreferences = getSharedPreferences("CountPrefs", Context.MODE_PRIVATE)
        val allEntries = sharedPreferences.all

        if (allEntries.isNotEmpty()) {
            for ((key, value) in allEntries) {
                try {
                    val data = value.toString().split(" - ")

                    if (data.size == 2) {
                        val count = data[0].toIntOrNull() ?: 0
                        val timestamp = data[1].toLongOrNull() ?: 0L
                        val formattedDate = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(timestamp))

                        // Create a horizontal row for each saved record
                        val recordLayout = LinearLayout(this)
                        recordLayout.orientation = LinearLayout.HORIZONTAL
                        recordLayout.setPadding(20, 10, 20, 10)
                        recordLayout.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        // Left Side: TextView for Name & Date
                        val textContainer = LinearLayout(this)
                        textContainer.orientation = LinearLayout.VERTICAL
                        textContainer.layoutParams = LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
                        )

                        val nameTextView = TextView(this)
                        nameTextView.text = key
                        nameTextView.textSize = 20f
                        nameTextView.setTextColor(Color.parseColor("#2E7D32")) // Green text

                        val dateTextView = TextView(this)
                        dateTextView.text = formattedDate
                        dateTextView.textSize = 14f
                        dateTextView.setTextColor(Color.GRAY)

                        // Add Name & Date to textContainer
                        textContainer.addView(nameTextView)
                        textContainer.addView(dateTextView)

                        // Right Side: Count Circle & Hamburger Menu
                        val rightContainer = LinearLayout(this)
                        rightContainer.orientation = LinearLayout.HORIZONTAL
                        rightContainer.setPadding(10, 0, 0, 0)

                        // Count Circle
                        val countTextView = TextView(this)
                        countTextView.text = count.toString()
                        countTextView.textSize = 18f
                        countTextView.setTextColor(Color.WHITE)
                        countTextView.gravity = android.view.Gravity.CENTER
                        countTextView.setBackgroundResource(R.drawable.circle_background) // Round shape
                        countTextView.layoutParams = LinearLayout.LayoutParams(100, 100)

                        // Hamburger Menu
                        val menuButton = ImageView(this)
                        menuButton.setImageResource(R.drawable.hamburger_menu)
                        menuButton.setPadding(20, 20, 20, 20)
                        menuButton.setOnClickListener { showPopupMenu(it, key, count) }

                        // Add elements to rightContainer
                        rightContainer.addView(countTextView)
                        rightContainer.addView(menuButton)

                        // Add left & right containers to recordLayout
                        recordLayout.addView(textContainer)
                        recordLayout.addView(rightContainer)

                        // Add record to layout
                        savedCountsLayout.addView(recordLayout)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error loading saved data!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun showPopupMenu(view: View, key: String, count: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.saved_count_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_continue -> {
                    val intent = Intent()
                    intent.putExtra("CONTINUE_COUNT", count)
                    intent.putExtra("CONTINUE_NAME", key) // Send the name
                    setResult(RESULT_OK, intent)
                    finish()
                    true
                }
                R.id.menu_delete -> {
                    val sharedPreferences = getSharedPreferences("CountPrefs", Context.MODE_PRIVATE)
                    val prefsEditor = sharedPreferences.edit()

                    prefsEditor.remove(key) // Fully delete the key
                    prefsEditor.commit() // Use commit() to ensure instant deletion

                    // Force reloading SharedPreferences after deletion
                    val refreshedEntries = sharedPreferences.all.filterValues { it.toString().isNotEmpty() }
                    if (refreshedEntries.isEmpty()) {
                        val intent = Intent()
                        intent.putExtra("LAST_ITEM_DELETED", true)
                        setResult(RESULT_OK, intent)
                    }

                    Toast.makeText(this, "Deleted: $key", Toast.LENGTH_SHORT).show()
                    recreate() // Refresh the list immediately
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}