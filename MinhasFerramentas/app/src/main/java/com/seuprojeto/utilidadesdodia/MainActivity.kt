package com.meusprojetos.minhasferramentas


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meusprojetos.minhasferramentas.R


/**
 * Atividade principal que exibe uma lista de ferramentas disponíveis.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Chamado quando a atividade está iniciando.
     *
     * @param savedInstanceState Se a atividade está sendo reiniciada após ter sido desligada anteriormente,
     * este Bundle contém os dados que ela forneceu mais recentemente em onSaveInstanceState(Bundle).
     * Caso contrário, é nulo.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTools)

        val tools = listOf(
            Tool(
                name = "Calculadora R$/kg",
                description = "Calcule valor de preço por kg",
                icon = R.drawable.calculator_48,
                activity = RegraTresActivity::class.java
            )
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ToolAdapter(this, tools)
    }
}
