package br.ufpr.matemticadivertida

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import kotlin.random.Random

class MaiorNumeroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_maior_numero)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        class MaiorNumeroActivity : AppCompatActivity() {

            // Componentes da Interface
            private lateinit var tvDigito1: TextView
            private lateinit var tvDigito2: TextView
            private lateinit var tvDigito3: TextView
            private lateinit var etResposta: TextInputEditText
            private lateinit var btnVerificar: Button

            // Variáveis de controle do jogo
            private var respostaCorreta: Int = 0
            private var rodadaAtual = 1
            private var acertos = 0
            private val TOTAL_RODADAS = 5

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                enableEdgeToEdge()
                setContentView(R.layout.activity_maior_numero)

                // Ajuste para telas modernas (Edge-to-Edge)
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
                // Limpa o campo de texto da rodada anterior
                etResposta.text?.clear()

                // Sorteia 3 dígitos aleatórios entre 0 e 9
                val d1 = Random.nextInt(0, 10)
                val d2 = Random.nextInt(0, 10)
                val d3 = Random.nextInt(0, 10)

                // Exibe na tela
                tvDigito1.text = d1.toString()
                tvDigito2.text = d2.toString()
                tvDigito3.text = d3.toString()

                // --- LÓGICA PRINCIPAL ---
                // Para achar o maior número, colocamos os dígitos em uma lista,
                // ordenamos de forma decrescente (do maior para o menor) e juntamos.
                // Exemplo: Sorteou 3, 9, 1 -> Ordena: 9, 3, 1 -> Resultado: 931
                val listaDigitos = listOf(d1, d2, d3)
                val listaOrdenada = listaDigitos.sortedDescending()

                // Concatena os números (String) e converte para Inteiro
                val maiorNumeroString = "${listaOrdenada[0]}${listaOrdenada[1]}${listaOrdenada[2]}"
                respostaCorreta = maiorNumeroString.toInt()
            }

            private fun processarResposta() {
                val textoDigitado = etResposta.text.toString()

                // Validação: Se o campo estiver vazio, avisa e não faz nada
                if (textoDigitado.isEmpty()) {
                    Toast.makeText(this, "Por favor, digite um número!", Toast.LENGTH_SHORT).show()
                    return
                }

                val respostaUsuario = textoDigitado.toInt()

                // Verifica se acertou
                if (respostaUsuario == respostaCorreta) {
                    acertos++
                    mostrarDialogo("Parabéns!", "Você acertou! O maior número é $respostaCorreta.")
                } else {
                    mostrarDialogo("Errou!", "Que pena! A resposta certa era $respostaCorreta.")
                }
            }

            private fun mostrarDialogo(titulo: String, mensagem: String) {
                AlertDialog.Builder(this)
                    .setTitle(titulo)
                    .setMessage(mensagem)
                    .setCancelable(false) // O usuário é obrigado a clicar no botão
                    .setPositiveButton("Próximo") { _, _ ->
                        avancarJogo()
                    }
                    .show()
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
                // Redireciona para a nova tela de ResultadoActivity que criamos
                val intent = android.content.Intent(this, ResultadoActivity::class.java)
                intent.putExtra("ACERTOS", acertos)
                intent.putExtra("TOTAL", TOTAL_RODADAS)
                startActivity(intent)
                finish()
            }
        }
    }
}