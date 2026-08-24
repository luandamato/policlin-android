package com.policlinsaude.newfeature.features.extractor.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.databinding.AdapterFactorExtractorCardsBinding
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorDetailModel
import com.policlinsaude.newfeature.utils.toDDMMYYYY

class FactorExtractorCardsAdapter(
    private val isCoPartFm: Boolean,
    private var list: MutableList<FactorExtractorDetailModel> = arrayListOf(),
) : RecyclerView.Adapter<FactorExtractorCardsAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterFactorExtractorCardsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(data: MutableList<FactorExtractorDetailModel>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterFactorExtractorCardsBinding) : RecyclerView.ViewHolder(binding.root) {

        private val adapter by lazy { FactorExtractorDetailAdapter(isCoPartFm) }

        fun bind(item: FactorExtractorDetailModel) {
            with(binding) {
                textviewNameLocale.text = item.prestador
                textviewDate.text = item.data?.toDDMMYYYY()
                recyclerViewFactorExtractorDetail.adapter = adapter
                adapter.update(item.itens)
            }
        }
    }

}