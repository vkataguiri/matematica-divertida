package br.ufpr.matemticadivertida

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import kotlin.random.Random

class MaiorNumeroActivity : AppCompatActivity() {

    private lateinit var tvDigito1: TextView
    private lateinit var tvDigito2: TextView
    private lateinit var tvDigito3: TextView
    private lateinit var etResposta: TextInputEditText
    private lateinit var btnVerificar: Button


    private var respostaCorreta: Int = 0
    private var rodadaAtual = 1
    private var acertos = 0
    private val TOTAL_RODADAS = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_maior_numero)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Vincular os componentes do XML
        tvDigito1 = findViewById(R.id.tvDigito1)
        tvDigito2 = findViewById(R.id.tvDigito2)
        tvDigito3 = findViewById(R.id.tvDigito3)
        etResposta = findViewById(R.id.etResposta)
        btnVerificar = findViewById(R.id.btnVerificar)

        // 2. Configurar o botão
        btnVerificar.setOnClickListener {
            processarResposta()
        }

        // 3. Iniciar a primeira rodada
        iniciarNovaRodada()
    }

    private fun iniciarNovaRodada() {
        etResposta.text?.clear()

        // Sorteia 3 dígitos aleatórios entre 0 e 9
        val d1 = Random.nextInt(0, 10)
        val d2 = Random.nextInt(0, 10)
        val d3 = Random.nextInt(0, 10)

        // Exibe na tela
        tvDigito1.text = d1.toString()
        tvDigito2.text = d2.toString()
        tvDigito3.text = d3.toString()

        // Lógica para encontrar o maior número possível
        val listaDigitos = listOf(d1, d2, d3)
        val listaOrdenada = listaDigitos.sortedDescending()
        val maiorNumeroString = "${listaOrdenada[0]}${listaOrdenada[1]}${listaOrdenada[2]}"
        respostaCorreta = maiorNumeroString.toInt()
    }

    private fun processarResposta() {
        val textoDigitado = etResposta.text.toString()

        if (textoDigitado.isEmpty()) {
            Toast.makeText(this, "Por favor, digite um número!", Toast.LENGTH_SHORT).show()
            return
        }

        val respostaUsuario = textoDigitado.toInt()

        if (respostaUsuario == respostaCorreta) {
            acertos++
            mostrarDialogo("Parabéns!", "Você acertou! O maior número é $respostaCorreta.")
        } else {
            mostrarDialogo("Errou!", "Que pena! A resposta certa era $respostaCorreta.")
        }
    }

    private fun mostrarDialogo(titulo: String, mensagem: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(mensagem)
        builder.setCancelable(false)
        builder.setPositiveButton("Próximo") { _, _ ->
            avancarJogo()
        }
        builder.show()
    }

    private fun avancarJogo() {
        if (rodadaAtual < TOTAL_RODADAS) {
            rodadaAtual++
            iniciarNovaRodada()
        } else {
            mostrarResultadoFinal()
        }
    }

    private fun mostrarResultadoFinal() {
        val intent = Intent(this, ResultadoActivity::class.java)
        intent.putExtra("ACERTOS", acertos)
        intent.putExtra("TOTAL", TOTAL_RODADAS)
        startActivity(intent)
        finish()
    }
}