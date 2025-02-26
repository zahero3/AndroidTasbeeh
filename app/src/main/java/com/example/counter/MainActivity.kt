package com.example.counter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.* import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private var count = 0
    private var continuedCountName: String? = null // Store continued count's name
    private lateinit var head3: TextView  // Reference to head3
    private lateinit var tvCounter: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firestore: FirebaseFirestore  // Firestore Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Find views
        tvCounter = findViewById(R.id.tvCounter)
        head3 = findViewById(R.id.head3)  // Initialize head3
        val btnCount: Button = findViewById(R.id.btnCount)
        val btnReset: Button = findViewById(R.id.btnReset)
        val btnSave: Button = findViewById(R.id.btnSave)
        val btnSaved: Button = findViewById(R.id.btnSaved)
        val playGameBtn: LinearLayout = findViewById(R.id.gmsbtn) // Find Play Game button

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("CountPrefs", Context.MODE_PRIVATE)

        // Start with 0 instead of loading a saved value
        count = 0
        tvCounter.text = count.toString()
        head3.visibility = View.GONE  // Hide head3 initially

        // Check if continuing from SavedCountsActivity
        val continueCount = intent.getIntExtra("CONTINUE_COUNT", -1)
        val continueName = intent.getStringExtra("CONTINUE_NAME")
        if (continueCount != -1 && continueName != null) {
            count = continueCount
            continuedCountName = continueName  // Store the continued name
            tvCounter.text = count.toString()
            updateHead3() // Update head3 visibility & text
        }

        // Play Game Button - Open QuizActivity
        playGameBtn.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        // Count Button (Vibrates when clicked)
        btnCount.setOnClickListener {
            count++
            tvCounter.text = count.toString()
            vibrateDevice() // Vibrate on click
            updateHead3() // Update head3 when count increases
        }

        // Reset Button
        btnReset.setOnClickListener {
            count = 0
            continuedCountName = null // Clear continued count name
            tvCounter.text = "0"
            head3.visibility = View.GONE // Hide head3
            Toast.makeText(this, "Counter Reset", Toast.LENGTH_SHORT).show()
        }

        // Save Button - Show Dialog Only If Not Continuing
        btnSave.setOnClickListener {
            if (continuedCountName != null) {
                saveCount(continuedCountName!!) // Save directly if continuing
            } else {
                showSaveDialog() // Show dialog for a new count
            }
        }

        // View Saved Counts
        btnSaved.setOnClickListener {
            val intent = Intent(this, SavedCountsActivity::class.java)
            startActivityForResult(intent, 100) // Use for receiving continued count
        }
    }

    // Function to show the save dialog
    private fun showSaveDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_save, null)
        val editText = dialogView.findViewById<EditText>(R.id.etSaveName)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("SAVE") { _, _ ->
                val name = editText.text.toString()
                if (name.isNotEmpty()) {
                    continuedCountName = name // Store name for future direct saving
                    saveCount(name)
                } else {
                    Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("CANCEL", null)
            .show()
    }

    // Function to save count in SharedPreferences and Firestore
    private fun saveCount(name: String) {
        val prefsEditor = sharedPreferences.edit()
        val timestamp = System.currentTimeMillis()

        // Save to SharedPreferences (Local Storage)
        prefsEditor.putString(name, "$count - $timestamp")
        prefsEditor.apply()

        // Save to Firestore
        val userId = "user_${System.currentTimeMillis()}"  // Unique key for each entry
        val countData = hashMapOf(
            "name" to name,
            "count" to count,
            "timestamp" to timestamp
        )

        firestore.collection("savedCounts").document(userId)
            .set(countData)
            .addOnSuccessListener {
                Toast.makeText(this, "Saved to Firestore!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save in Firestore!", Toast.LENGTH_SHORT).show()
            }
    }

    // Function to make the device vibrate
    private fun vibrateDevice() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(100) // Deprecated in newer versions but needed for older devices
            }
        }
    }

    // Function to update head3 visibility & text
    private fun updateHead3() {
        if (count > 0) {
            head3.visibility = View.VISIBLE
            head3.text = if (continuedCountName != null) {
                "Save: $continuedCountName"
            } else {
                getString(R.string.head3) // Default text if no saved count
            }
        } else {
            head3.visibility = View.GONE
        }
    }

    // Handling returned count from SavedCountsActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val continuedCount = data?.getIntExtra("CONTINUE_COUNT", -1) ?: -1
            val continuedName = data?.getStringExtra("CONTINUE_NAME")
            val lastItemDeleted = data?.getBooleanExtra("LAST_ITEM_DELETED", false) ?: false

            if (lastItemDeleted) {
                continuedCountName = null // Reset continued name when last item is deleted
            }

            if (continuedCount != -1 && continuedName != null) {
                count = continuedCount
                continuedCountName = continuedName // Store name to skip dialog next time
                tvCounter.text = count.toString()
            }
        }
    }

}