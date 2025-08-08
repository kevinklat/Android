package com.meusprojetos.minhasferramentas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meusprojetos.minhasferramentas.R

/**
 * Adaptador para a RecyclerView que exibe uma lista de ferramentas.
 *
 * @param context O contexto atual.
 * @param tools A lista de ferramentas a ser exibida.
 */
class ToolAdapter(
    private val context: Context,
    private val tools: List<Tool>
) : RecyclerView.Adapter<ToolAdapter.ToolViewHolder>() {

    /**
     * ViewHolder para os itens da lista de ferramentas.
     *
     * @param itemView A view do item.
     */
    inner class ToolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** O ícone da ferramenta. */
        val toolIcon: ImageView = itemView.findViewById(R.id.toolIcon)
        /** O nome da ferramenta. */
        val toolName: TextView = itemView.findViewById(R.id.toolName)
        /** A descrição da ferramenta. */
        val toolDescription: TextView = itemView.findViewById(R.id.toolDescription)
    }

    /**
     * Chamado quando a RecyclerView precisa de um novo [ToolViewHolder] do tipo fornecido
     * ara representar um item.
     *
     * @param parent O ViewGroup no qual a nova View será adicionada após ser vinculada a
     * uma posição do adaptador.
     * @param viewType O tipo de view da nova View.
     * @return Um novo [ToolViewHolder] que contém uma View do tipo de view fornecido.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_tool,
            parent, false)
        return ToolViewHolder(view)
    }

    /**
     * Chamado pela RecyclerView para exibir os dados na posição especificada. Este método deve
     * atualizar o conteúdo do [ToolViewHolder.itemView] para refletir o item na
     * posição fornecida.
     *
     * @param holder O [ToolViewHolder] que deve ser atualizado para representar o
     * conteúdo do item na posição fornecida no conjunto de dados.
     * @param position A posição do item no conjunto de dados do adaptador.
     */
    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        val tool = tools[position]
        holder.toolIcon.setImageResource(tool.icon)
        holder.toolName.text = tool.name
        holder.toolDescription.text = tool.description

        holder.itemView.setOnClickListener {
            val intent = Intent(context, tool.activity)
            context.startActivity(intent)
        }
    }

    /**
     * Retorna o número total de itens no conjunto de dados mantido pelo adaptador.
     *
     * @return O número total de itens neste adaptador.
     */
    override fun getItemCount(): Int = tools.size
}

