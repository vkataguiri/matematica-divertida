package br.ufpr.matemticadivertida

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

// Definição da classe de dados (pode ficar fora da Activity)
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

    // Variáveis de controle do jogo
    private var questoesDaRodada = listOf<QuestaoContagem>()
    private var indiceAtual = 0
    private var acertos = 0
    private val TOTAL_PERGUNTAS = 5

    // Banco de questões (Seus 11 grupos de imagens estão aqui)
    private val todasAsQuestoes = listOf(
        // Exemplo 1: Imagem de Carros
        QuestaoContagem(R.drawable.avenida, "Quantos táxis amarelos estão na imagem?", 4),
        QuestaoContagem(R.drawable.avenida, "Quantos carros de polícia você encontra?", 2),
        QuestaoContagem(R.drawable.avenida, "Quantos caminhões de bombeiros existem?", 2),
        QuestaoContagem(R.drawable.avenida, "Quantos semáforos de trânsito você vê?", 3),
        QuestaoContagem(R.drawable.avenida, "Quantas placas de 'PARE' (STOP) existem?", 1),

        // Fábrica
        QuestaoContagem(R.drawable.fabrica, "Quantos robôs amarelos estão trabalhando?", 5),
        QuestaoContagem(R.drawable.fabrica, "Quantas empilhadeiras azuis você vê?", 3),
        QuestaoContagem(R.drawable.fabrica, "Quantos caminhões VERDES estão lá fora?", 2),
        QuestaoContagem(R.drawable.fabrica, "Quantos caminhões estão lá fora ao todo?", 4),
        QuestaoContagem(R.drawable.fabrica, "Quantas engrenagens cinzas estão nas paredes?", 6),

        // Fazenda
        QuestaoContagem(R.drawable.fazenda, "Quantos patos estão nadando na lagoa?", 5),
        QuestaoContagem(R.drawable.fazenda, "Quantos pássaros azuis estão no galho?", 4),
        QuestaoContagem(R.drawable.fazenda, "Quantos coelhos você vê na grama?", 3),
        QuestaoContagem(R.drawable.fazenda, "Quantos porcos estão no chiqueiro?", 4),
        QuestaoContagem(R.drawable.fazenda, "Quantas ovelhas existem ao todo?", 3),

        // Sala de aula
        QuestaoContagem(R.drawable.saladeaula, "Quantos relógios estão na parede?", 3),
        QuestaoContagem(R.drawable.saladeaula, "Quantas mochilas estão penduradas?", 5),
        QuestaoContagem(R.drawable.saladeaula, "Quantos gatos estão dormindo na sala?", 2),
        QuestaoContagem(R.drawable.saladeaula, "Quantos globos terrestres existem na sala?", 3),
        QuestaoContagem(R.drawable.saladeaula,"Quantos desenhos estão no quadro de avisos?", 8),

        // Praia
        QuestaoContagem(R.drawable.praia, "Quantos golfinhos estão pulando no mar?", 3),
        QuestaoContagem(R.drawable.praia, "Quantos caranguejos vermelhos estão na areia?", 2),
        QuestaoContagem(R.drawable.praia, "Quantas pranchas de surf estão paradas na areia?", 2),
        QuestaoContagem(R.drawable.praia, "Quantas crianças estão jogando vôlei?", 4),
        QuestaoContagem(R.drawable.praia, "Quantos faróis existem ao fundo?", 1),

        // Indio
        QuestaoContagem(R.drawable.indio, "Quantas araras coloridas estão voando?", 3),
        QuestaoContagem(R.drawable.indio, "Quantos macacos estão nas árvores?", 5),
        QuestaoContagem(R.drawable.indio, "Quantas capivaras estão na margem do rio?", 3),
        QuestaoContagem(R.drawable.indio, "Quantos cocares estão pendurados na oca?", 4),
        QuestaoContagem(R.drawable.indio, "Quantas onças estão deitadas na grama?", 2),

        // Parque
        QuestaoContagem(R.drawable.parque, "Quantos patos estão nadando no lago?", 3),
        QuestaoContagem(R.drawable.parque, "Quantas crianças estão nos balanços?", 3),
        QuestaoContagem(R.drawable.parque, "Quantos cachorros estão passeando no parque?", 3),
        QuestaoContagem(R.drawable.parque, "Quantas crianças estão andando de bicicleta?", 2),
        QuestaoContagem(R.drawable.parque, "Quantas pipas estão voando no céu?", 1),

        // Fundo do mar
        QuestaoContagem(R.drawable.fundodomar, "Quantas tartarugas marinhas estão na areia?", 2),
        QuestaoContagem(R.drawable.fundodomar, "Quantos peixes azuis (tipo a Dory) estão nadando?", 3),
        QuestaoContagem(R.drawable.fundodomar, "Quantos polvos estão escondidos nas tocas?", 3),
        QuestaoContagem(R.drawable.fundodomar, "Quantos cavalos-marinhos amarelos você vê?", 6),
        QuestaoContagem(R.drawable.fundodomar, "Quantos peixes listrados amarelos (canto superior)?", 3),

        // Games
        QuestaoContagem(R.drawable.games, "Quantas televisões estão ligadas?", 4),
        QuestaoContagem(R.drawable.games, "Quantos posters de jogos há na parede?", 4),
        QuestaoContagem(R.drawable.games, "Quantas crianças estão no sofá vermelho?", 3),
        QuestaoContagem(R.drawable.games, "Quantos posters de Minecraft existem?", 2),
        QuestaoContagem(R.drawable.games, "Quantos puffs amarelos existem?", 1),

        // Doces
        QuestaoContagem(R.drawable.doces, "Quantos donuts estão nas prateleiras?", 6),
        QuestaoContagem(R.drawable.doces, "Quantos carrinhos de sorvete você vê?", 1),
        QuestaoContagem(R.drawable.doces, "Quantos bolos grandes estão na vitrine?", 2),
        QuestaoContagem(R.drawable.doces, "Quantos sorvetes de casquinha estão no carrinho?", 3),
        QuestaoContagem(R.drawable.doces, "Quantos ursinhos de goma NÃO são amarelos?", 4),

        // Pets
        QuestaoContagem(R.drawable.pets, "Quantos cachorros estão deitados na almofada?", 1),
        QuestaoContagem(R.drawable.pets, "Quantos coelhos estão pulando na grama?", 2),
        QuestaoContagem(R.drawable.pets, "Quantos pássaros você vê nas gaiolas?", 3),
        QuestaoContagem(R.drawable.pets, "Quantos peixes estão dentro do aquário?", 5),
        QuestaoContagem(R.drawable.pets, "Quantos hamsters estão brincando na cena?", 6)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contagem)

        // Ajuste para Edge-to-Edge (barra de status transparente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        // 2. Embaralha as IMAGENS disponíveis e pega a quantidade necessária (5)
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
        // Gera opções erradas até ter 3 números únicos
        while (opcoes.size < 3) {
            val errado = Random.nextInt(0, 11) // Gera números entre 0 e 10
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
            .setCancelable(false) // Impede fechar clicando fora
            .show()
    }

    private fun mostrarResultadoFinal() {
        // Evita divisão por zero se TOTAL_PERGUNTAS fosse 0 (segurança)
        val nota = if (TOTAL_PERGUNTAS > 0) (acertos * 100) / TOTAL_PERGUNTAS else 0

        AlertDialog.Builder(this)
            .setTitle("Fim de Jogo")
            .setMessage("Você acertou $acertos de $TOTAL_PERGUNTAS.\nNota: $nota")
            .setPositiveButton("Voltar") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }
}