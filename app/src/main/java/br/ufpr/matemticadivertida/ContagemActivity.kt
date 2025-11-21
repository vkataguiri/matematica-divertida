package br.ufpr.matemticadivertida

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlin.random.Random

class ContagemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contagem)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Atualizado: Agora inclui o texto da pergunta específica
    data class QuestaoContagem(
        val imagemResId: Int,
        val textoPergunta: String,
        val respostaCorreta: Int
    )

    class ContagemActivity : AppCompatActivity() {

        private lateinit var tvPergunta: TextView
        private lateinit var ivImagem: ImageView
        private lateinit var btnOpcao1: Button
        private lateinit var btnOpcao2: Button
        private lateinit var btnOpcao3: Button

        // Banco de questões: Várias perguntas podem usar a mesma imagem (ex: img_carros)
        // ATENÇÃO: Substitua R.drawable.ic_launcher_foreground pelas suas imagens reais (ex: R.drawable.img_carros)
        private val todasAsQuestoes = listOf(
            // Exemplo 1: Imagem de Carros (usando ícone padrão como placeholder)
            QuestaoContagem(R.drawable.avenida, "Quantos carros há ao todo?", 5),
            QuestaoContagem(R.drawable.ic_launcher_foreground, "Quantos carros são AZUIS?", 2),

            // Exemplo 2: Imagem de Frutas
            QuestaoContagem(R.drawable.ic_launcher_foreground, "Quantas frutas você vê?", 8),
            QuestaoContagem(R.drawable.ic_launcher_foreground, "Quantas maçãs existem?", 3),

            // Exemplo 3: Imagem de Bolas
            QuestaoContagem(R.drawable.ic_launcher_foreground, "Conte todas as bolas:", 10),

            // Adicione mais variações para suas 10 imagens...
            QuestaoContagem(R.drawable.ic_launcher_foreground, "Quantos animais na floresta?", 4),
            QuestaoContagem(R.drawable.ic_launcher_foreground, "Quantos pássaros estão voando?", 1),
            QuestaoContagem(R.drawable.ic_launcher_foreground, "Total de itens na imagem:", 6),
            QuestaoContagem(R.drawable.ic_launcher_foreground, "Quantas estrelas amarelas?", 7),
            QuestaoContagem(R.drawable.ic_launcher_foreground, "Quantos peixes no aquário?", 9)
        )

        private var questoesDaRodada = listOf<QuestaoContagem>()
        private var indiceAtual = 0
        private var acertos = 0
        private val TOTAL_PERGUNTAS = 5

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_contagem)

            // Vincular componentes
            tvPergunta = findViewById(R.id.tvPerguntaContagem)
            ivImagem = findViewById(R.id.ivImagem)
            btnOpcao1 = findViewById(R.id.btnOpcao1)
            btnOpcao2 = findViewById(R.id.btnOpcao2)
            btnOpcao3 = findViewById(R.id.btnOpcao3)

            iniciarJogo()
        }

        private fun iniciarJogo() {
            questoesDaRodada = sortearQuestoesUnicasPorImagem()
            indiceAtual = 0
            acertos = 0

            exibirQuestao()
        }

        // Lógica para garantir 5 imagens diferentes, mas variando a pergunta
        private fun sortearQuestoesUnicasPorImagem(): List<QuestaoContagem> {
            // 1. Agrupa todas as questões pelo ID da imagem
            val questoesPorImagem = todasAsQuestoes.groupBy { it.imagemResId }

            // 2. Embaralha as IMAGENS disponíveis e pega 5
            val imagensSorteada = questoesPorImagem.keys.shuffled().take(TOTAL_PERGUNTAS)

            // 3. Para cada imagem sorteada, escolhe UMA pergunta aleatória associada a ela
            val selecaoFinal = ArrayList<QuestaoContagem>()
            for (imgId in imagensSorteada) {
                val variantesDaImagem = questoesPorImagem[imgId]
                if (variantesDaImagem != null) {
                    selecaoFinal.add(variantesDaImagem.random())
                }
            }

            return selecaoFinal
        }

        private fun exibirQuestao() {
            if (indiceAtual >= questoesDaRodada.size) {
                mostrarResultadoFinal()
                return
            }

            val questaoAtual = questoesDaRodada[indiceAtual]

            // Atualiza a interface
            tvPergunta.text = questaoAtual.textoPergunta
            ivImagem.setImageResource(questaoAtual.imagemResId)

            // Gera opções e configura botões
            val respostaCorreta = questaoAtual.respostaCorreta
            val opcoes = gerarOpcoes(respostaCorreta)

            configurarBotao(btnOpcao1, opcoes[0], respostaCorreta)
            configurarBotao(btnOpcao2, opcoes[1], respostaCorreta)
            configurarBotao(btnOpcao3, opcoes[2], respostaCorreta)
        }

        private fun gerarOpcoes(correta: Int): List<Int> {
            val opcoes = mutableSetOf<Int>()
            opcoes.add(correta)
            while (opcoes.size < 3) {
                val errado = Random.nextInt(0, 11) // Gera erros entre 0 e 10
                if (errado != correta) opcoes.add(errado)
            }
            return opcoes.toList().shuffled()
        }

        private fun configurarBotao(botao: Button, valor: Int, correta: Int) {
            botao.text = valor.toString()
            botao.setOnClickListener {
                verificarResposta(valor, correta)
            }
        }

        private fun verificarResposta(escolha: Int, correta: Int) {
            val titulo: String
            val mensagem: String

            if (escolha == correta) {
                acertos++
                titulo = "Parabéns!"
                mensagem = "Resposta correta."
            } else {
                titulo = "Errou!"
                mensagem = "A resposta certa era $correta."
            }

            AlertDialog.Builder(this)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton("Próxima") { _, _ ->
                    indiceAtual++
                    exibirQuestao()
                }
                .setCancelable(false)
                .show()
        }

        private fun mostrarResultadoFinal() {
            val nota = (acertos * 100) / TOTAL_PERGUNTAS
            AlertDialog.Builder(this)
                .setTitle("Fim de Jogo")
                .setMessage("Você acertou $acertos de $TOTAL_PERGUNTAS.\nNota: $nota")
                .setPositiveButton("Voltar") { _, _ -> finish() }
                .setCancelable(false)
                .show()
        }
    }
}