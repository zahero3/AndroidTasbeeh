package com.example.counter

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class QuizActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var etAnswer: EditText
    private lateinit var btnNext: Button

    private val firestore = FirebaseFirestore.getInstance()
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
    private var userAnswers = mutableListOf<String>() // Store answers
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        tvQuestion = findViewById(R.id.tvQuestion)
        etAnswer = findViewById(R.id.etAnswer)
        btnNext = findViewById(R.id.btnNext)

        // Randomly select 5 questions
        selectedQuestions = allQuestions.shuffled().take(5).toMutableList()
        showNextQuestion()

        btnNext.setOnClickListener {
            val answer = etAnswer.text.toString().trim()

            if (answer.isEmpty()) {
                Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show()
            } else {
                userAnswers.add(answer) // Store answer

                // Move to the next question
                currentQuestionIndex++
                if (currentQuestionIndex < selectedQuestions.size) {
                    showNextQuestion()
                } else {
                    saveQuizAnswers()
                }
            }
        }
    }

    private fun showNextQuestion() {
        etAnswer.setText("") // Clear previous answer
        tvQuestion.text = selectedQuestions[currentQuestionIndex]
    }

    private fun saveQuizAnswers() {
        val timestamp = System.currentTimeMillis()
        val userId = "user_$timestamp"

        val quizData = hashMapOf(
            "username" to "Anonymous",  // Change if using authentication
            "questions" to selectedQuestions,
            "answers" to userAnswers,
            "timestamp" to timestamp
        )

        firestore.collection("quizResponses").document(userId)
            .set(quizData)
            .addOnSuccessListener {
                Toast.makeText(this, "Quiz Submitted!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("USER_ID", userId) // Pass user ID to result page
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save quiz!", Toast.LENGTH_SHORT).show()
            }
    }
}