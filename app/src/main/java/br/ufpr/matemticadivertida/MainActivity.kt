package br.ufpr.matemticadivertida

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Buttons
        val btnContagem = findViewById<Button>(R.id.btnContagem)
        val btnAritmetica = findViewById<Button>(R.id.btnAritmetica)
        val btnMaiorNumero = findViewById<Button>(R.id.btnMaiorNumero)

        // open counting game
        btnContagem.setOnClickListener {
            val intent = Intent(this, ContagemActivity::class.java)
            startActivity(intent)
        }

        // open arithmetics game
        btnAritmetica.setOnClickListener {
            val intent = Intent(this, AritmeticaActivity::class.java)
            startActivity(intent)
        }

        // open largest number game
        btnMaiorNumero.setOnClickListener {
            val intent = Intent(this, MaiorNumeroActivity::class.java)
            startActivity(intent)
        }
    }
}