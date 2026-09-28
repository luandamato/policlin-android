package br.com.policlinsaude.ui.activities.factorExtractor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.data.models.FactorExtractorDetailModel
import br.com.policlinsaude.databinding.AdapterFactorExtractorCardsBinding
import br.com.policlinsaude.ui.activities.factorExtractor.FactorExtractorDetailAdapter
import br.com.policlinsaude.util.extensions.toDDMMYYYY

class FactorExtractorCardsAdapter(
    private val isCoPartFm: Boolean
) : RecyclerView.Adapter<FactorExtractorCardsAdapter.ViewHolder>() {

    private val list = mutableListOf<FactorExtractorDetailModel>()

    fun update(data: List<FactorExtractorDetailModel>?) {
        list.clear()
        if (!data.isNullOrEmpty()) list.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterFactorExtractorCardsBinding.inflate(
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
        private val binding: AdapterFactorExtractorCardsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val detailAdapter by lazy { FactorExtractorDetailAdapter(isCoPartFm) }

        fun bind(item: FactorExtractorDetailModel) {
            binding.textviewDate.text = item.data?.toDDMMYYYY() ?: ""
            binding.textviewNameLocale.text = item.prestador
            binding.recyclerViewFactorExtractorDetail.adapter = detailAdapter
            detailAdapter.update(item.itens)
        }
    }
}