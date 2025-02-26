package com.example.counter

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class ResultActivity : AppCompatActivity() {

    private lateinit var imgCelebrity: ImageView
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        imgCelebrity = findViewById(R.id.imgCelebrity)
        firestore = FirebaseFirestore.getInstance()
        val btnClose: Button = findViewById(R.id.btnCloseResult)

        // List of celebrity images
        val celebrityImages = arrayOf(
            R.drawable.celb5
        )
        imgCelebrity.setImageResource(celebrityImages[Random.nextInt(celebrityImages.size)])

        // Close Button - Returns to MainActivity
        btnClose.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}