package br.com.policlinsaude.ui.activities.factorExtractor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.data.models.FactorExtractorDetailItemsModel
import br.com.policlinsaude.databinding.AdapterFactorExtractorDetailBinding
import br.com.policlinsaude.util.extensions.toCurrencyBRL

class FactorExtractorDetailAdapter(
    private val isCoPartFm: Boolean
) : RecyclerView.Adapter<FactorExtractorDetailAdapter.ViewHolder>() {

    private val list = mutableListOf<FactorExtractorDetailItemsModel>()

    fun update(data: List<FactorExtractorDetailItemsModel>?) {
        list.clear()
        if (!data.isNullOrEmpty()) list.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterFactorExtractorDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(
        private val binding: AdapterFactorExtractorDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FactorExtractorDetailItemsModel) {
            binding.textviewCodeTussFactorDetail.text = item.codigo
            binding.textviewDescriptionGroupFactorDetail.text = item.copartDes
            binding.textviewFeValue.text = item.valor.toCurrencyBRL()
        }
    }
}