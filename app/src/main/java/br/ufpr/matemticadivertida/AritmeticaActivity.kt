package br.ufpr.matemticadivertida

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlin.random.Random
import kotlin.random.nextInt

class AritmeticaActivity : AppCompatActivity() {
    private lateinit var tvPergunta: TextView
    private lateinit var etResposta: EditText
    private lateinit var btnResponder: Button

    private var num1 = 0
    private var num2 = 0
    private var operation = "" // plus or minus
    private var correctAnswer = 0

    private var questionAmount = 0
    private var correctAnswers = 0
    private val TOTAL_QUESTIONS = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aritmetica)

        tvPergunta = findViewById(R.id.tvPergunta)
        etResposta = findViewById(R.id.etResposta)
        btnResponder = findViewById(R.id.btnResponder)

        generateQuestion()

        btnResponder.setOnClickListener {
            verifyAnswer()
        }
    }

    private fun generateQuestion() {
        if (questionAmount >= TOTAL_QUESTIONS) {
            showFinalResult()
            return
        }

        etResposta.text.clear()

        // Gera os números temporariamente
        var val1 = Random.nextInt(0, 10)
        var val2 = Random.nextInt(0, 10)
        val isPlus = Random.nextBoolean()

        if (isPlus) {
            operation = "+"
            num1 = val1
            num2 = val2
            correctAnswer = num1 + num2
        } else {
            operation = "-"

            //Lógica Anti-negativo
            if (val1 < val2) {
                num1 = val2
                num2 = val1
            } else {
                num1 = val1
                num2 = val2
            }

            correctAnswer = num1 - num2
        }

        tvPergunta.text = "$num1 $operation $num2"
        questionAmount++
    }

    private fun verifyAnswer() {
        val answerText = etResposta.text.toString()

        if (answerText.isEmpty()) {
            Toast.makeText(this, "Digite uma resposta", Toast.LENGTH_SHORT).show()
            return
        }

        val userAnswer = answerText.toInt()

        val title: String
        val message: String

        if (userAnswer == correctAnswer) {
            correctAnswers++
            title = "Parabéns!"
            message = "Sua resposta está correta."
        } else {
            title = "Errou!"
            message = "Que pena. A resposta correta era $correctAnswer."
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Próxima") { dialog, _ ->
            generateQuestion()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun showFinalResult() {
        val intent = android.content.Intent(this, ResultadoActivity::class.java)
        intent.putExtra("ACERTOS", correctAnswers)
        intent.putExtra("TOTAL", TOTAL_QUESTIONS)
        startActivity(intent)
        finish()
    }
}