package br.ufpr.matemticadivertida

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        val tvResultadoTexto = findViewById<TextView>(R.id.tvResultadoTexto)
        val tvNotaFinal = findViewById<TextView>(R.id.tvNotaFinal)
        val btnVoltar = findViewById<Button>(R.id.btnVoltarMenu)

        // Recupera os dados enviados
        val acertos = intent.getIntExtra("ACERTOS", 0)
        val total = intent.getIntExtra("TOTAL", 0)

        // Calcula a nota com segurança
        val nota = if (total > 0) (acertos * 100) / total else 0

        tvResultadoTexto.text = "Você acertou $acertos de $total questões"
        tvNotaFinal.text = "Nota: $nota"

        btnVoltar.setOnClickListener {
            finish()
        }
    }
}