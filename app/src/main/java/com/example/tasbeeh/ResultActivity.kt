package com.example.tasbeeh

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
import android.widget.Button
import android.content.Intent

class ResultActivity : AppCompatActivity() {

    private lateinit var imgCelebrity: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        imgCelebrity = findViewById(R.id.imgCelebrity)

        // List of celebrity images in drawable folder
        val celebrityImages = arrayOf(
            R.drawable.celb5
        )

        // Pick a random image
        val randomIndex = Random.nextInt(celebrityImages.size)
        imgCelebrity.setImageResource(celebrityImages[randomIndex])


        val btnClose: Button = findViewById(R.id.btnCloseResult)

        // Close Button - Returns to MainActivity
        btnClose.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // Close ResultActivity
        }
    }
}