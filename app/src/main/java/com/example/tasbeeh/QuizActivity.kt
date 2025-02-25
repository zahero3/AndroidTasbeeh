package com.example.tasbeeh

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var etAnswer: EditText
    private lateinit var btnNext: Button

    private val allQuestions = listOf(
        "What is your favorite color?",
        "Which country do you want to visit?",
        "What is your dream job?",
        "What is your favorite hobby?",
        "If you had a superpower, what would it be?",
        "Who is your role model?",
        "What is your favorite movie?",
        "What is your favorite food?",
        "What makes you happy?",
        "What is your biggest achievement?"
    )

    private var selectedQuestions = mutableListOf<String>()
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        tvQuestion = findViewById(R.id.tvQuestion)
        etAnswer = findViewById(R.id.etAnswer)
        btnNext = findViewById(R.id.btnNext)

        // Randomly select 5 questions from the list of 10
        selectedQuestions = allQuestions.shuffled().take(5).toMutableList()

        // Show the first question
        showNextQuestion()

        btnNext.setOnClickListener {
            if (etAnswer.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show()
            } else {
                // Move to the next question
                currentQuestionIndex++
                if (currentQuestionIndex < selectedQuestions.size) {
                    showNextQuestion()
                } else {
                    // When all questions are answered, move to ResultActivity
                    val intent = Intent(this, ResultActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun showNextQuestion() {
        etAnswer.setText("") // Clear previous answer
        tvQuestion.text = selectedQuestions[currentQuestionIndex]
    }
}