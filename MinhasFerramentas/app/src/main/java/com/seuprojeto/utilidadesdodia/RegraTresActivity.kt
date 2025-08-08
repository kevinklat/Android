package com.meusprojetos.minhasferramentas

import android.content.Context // Necessário para MODE_PRIVATE se não estiver dentro de Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.meusprojetos.minhasferramentas.R

/**
 * Activity para calcular regra de três simples, especificamente para converter
 * o preço de um produto com base em seu peso para o preço por KG.
 * Mantém um histórico dos cálculos realizados.
 */
class RegraTresActivity : AppCompatActivity() {

    private val historicoCalculos = mutableListOf<String>()
    private lateinit var txtHistorico: TextView

    companion object {
        private const val PREFS_NAME = "HistoricoRegraTresPrefs"
        private const val KEY_HISTORICO = "historico_calculos"
    }

    /**
     * Chamado quando a activity é criada pela primeira vez.
     * Configura a interface do usuário, listeners de eventos e carrega o histórico de cálculos.
     * @param savedInstanceState Se a activity estiver sendo recriada após uma destruição anterior,
     * este Bundle contém os dados mais recentes fornecidos em onSaveInstanceState(Bundle).
     * Caso contrário, é nulo.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regra_tres)

        val gramasProduto = findViewById<EditText>(R.id.valorA)
        val valorProduto = findViewById<EditText>(R.id.valorB)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        val txtResultado = findViewById<TextView>(R.id.txtResultado)
        txtHistorico = findViewById(R.id.txtHistorico)

        carregarHistorico() // Carrega o histórico
        atualizarTextViewHistorico() // Exibe o histórico (carregado ou inicial)

        btnCalcular.setOnClickListener {
            val gramas = gramasProduto.text.toString().toDoubleOrNull()
            val valor = valorProduto.text.toString().toDoubleOrNull()

            if (gramas != null && valor != null && gramas > 0) {
                val valorPorGrama = valor / gramas
                val valorPorKg = valorPorGrama * 1000
                val resultadoFormatado = "Valor por KG: R$ %.2f".format(valorPorKg)
                txtResultado.text = resultadoFormatado

                val entradaHistorico =
                    "Gramas: $gramas, Valor: R$ %.2f -> %s".format(valor, resultadoFormatado)
                adicionarAoHistorico(entradaHistorico)
            } else {
                txtResultado.text = "Por favor, insira as gramas e o valor do produto corretamente."
            }
        }

        val btnLimparHistorico = findViewById<Button>(R.id.btnLimparHistorico)
        btnLimparHistorico.setOnClickListener {
            limparHistorico()
        }

    }

    /**
     * Limpa o histórico de cálculos, tanto da memória quanto das SharedPreferences.
     * Atualiza a interface do usuário para refletir a limpeza.
     */
    private fun limparHistorico() {
        historicoCalculos.clear() // Limpa a lista na memória
        atualizarTextViewHistorico() // Atualiza a UI
        salvarHistorico() // Remove também das SharedPreferences
        // Você pode querer mostrar uma confirmação (AlertDialog) antes de limpar.
    }

    /**
     * Adiciona uma nova entrada ao histórico de cálculos.
     * Limita o tamanho do histórico a 20 entradas, removendo a mais antiga se necessário.
     * Atualiza a interface do usuário.
     * @param entrada A string que representa o cálculo a ser adicionado ao histórico.
     */
    private fun adicionarAoHistorico(entrada: String) {
        historicoCalculos.add(0, entrada)
        if (historicoCalculos.size > 15) { // Limite opcional
            historicoCalculos.removeAt(historicoCalculos.size - 1)
        }
        atualizarTextViewHistorico()
        // Opcional: Salvar imediatamente após adicionar,
        // mas salvar em onStop é geralmente suficiente e mais eficiente.
        // salvarHistorico()
    }

    /**
     * Atualiza o TextView que exibe o histórico de cálculos.
     * Se o histórico estiver vazio, exibe "Histórico:". Caso contrário,
     * exibe cada entrada em uma nova linha.
     */
    private fun atualizarTextViewHistorico() {
        if (historicoCalculos.isEmpty()) {
            txtHistorico.text = "Histórico:"
        } else {
            val textoHistorico = StringBuilder("Histórico:")
            historicoCalculos.forEach { entrada ->
                textoHistorico.append(entrada).append("")
            }
            txtHistorico.text = textoHistorico.toString()
        }
    }

    /**
     * Salva o histórico de cálculos atual nas SharedPreferences.
     */
    private fun salvarHistorico() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet(KEY_HISTORICO, historicoCalculos.toSet())
        editor.apply()
    }

    /**
     * Carrega o histórico de cálculos salvo nas SharedPreferences.
     * Os itens são carregados e uma tentativa de ordenação
     * é feita para manter os mais recentes no topo.
     */
    private fun carregarHistorico() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val calculosSalvos = sharedPreferences.getStringSet(KEY_HISTORICO, null)
        calculosSalvos?.let {
            historicoCalculos.clear()
            // A ordem do Set não é garantida. Adicionamos os itens e confiamos que
            // a lógica de adicionarAoHistorico (add(0, ...)) e a exibição manterão
            // os mais recentes no topo durante a sessão.
            // Para restaurar a ordem entre sessões de forma mais fiel com Set,
            // precisaríamos de prefixos ou timestamps, ou usar JSON.
            historicoCalculos.addAll(
                it.toList().sortedDescending()
            ) // Tentativa de ordenar, pode não ser ideal
        }
    }

    /**
     * Chamado quando a activity está prestes a ser interrompida.
     * Salva o histórico de cálculos.
     */
    override fun onStop() {
        super.onStop()
        salvarHistorico()
    }
}
