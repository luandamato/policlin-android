package com.policlinsaude.newfeature.features.extractor.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.policlinsaude.newfeature.databinding.AdapterFactorExtractorDetailBinding
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorDetailItemsModel
import com.policlinsaude.newfeature.utils.toCurrencyBRL


class FactorExtractorDetailAdapter(
    private var list: MutableList<FactorExtractorDetailItemsModel> = arrayListOf(),
) : RecyclerView.Adapter<FactorExtractorDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactorExtractorDetailAdapter.ViewHolder {
        return ViewHolder(AdapterFactorExtractorDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FactorExtractorDetailAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(data: MutableList<FactorExtractorDetailItemsModel>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: AdapterFactorExtractorDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FactorExtractorDetailItemsModel) {
            with(binding) {
                textviewCodeTussFactorDetail.text = item.codigo
                textviewProcedureTussFactorDetail.text = item.descricao
                textviewDescriptionGroupFactorDetail.text = item.copartDes
                textviewValueFactorDetail.text = item.valor.toCurrencyBRL()
            }
        }
    }

}