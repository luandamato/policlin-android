package br.com.policlinsaude.ui.activities.incometax

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.data.models.IncomeTaxItemModel
import br.com.policlinsaude.databinding.AdapterIncomeTaxItemBinding

/**
 * Adapter da lista de informes de Imposto de Renda.
 * Migrado do legado `IncomeTaxItemAdapter` (stack `com.policlinsaude.newfeature`).
 */
class IncomeTaxItemAdapter(
    private var list: MutableList<IncomeTaxItemModel> = arrayListOf(),
    var setOnClickListener: (IncomeTaxItemModel?) -> Unit = {},
) : RecyclerView.Adapter<IncomeTaxItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterIncomeTaxItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(data: MutableList<IncomeTaxItemModel>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterIncomeTaxItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: IncomeTaxItemModel) {
            with(binding) {
                textviewYear.text = item.descricao.toString()

                root.setOnClickListener {
                    setOnClickListener(item)
                }
            }
        }
    }
}