package com.policlinsaude.newfeature.features.coparticipation.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.databinding.AdapterResearshCoParticipationBinding
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationItemsDetails
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorDetailItemsModel
import com.policlinsaude.newfeature.utils.toCurrencyBRL


class ResearchCoParticipationItemsAdapter(
    private val isCoPartFM: Boolean,
    private var list: MutableList<CoParticipationItemsDetails> = arrayListOf(),
) : RecyclerView.Adapter<ResearchCoParticipationItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResearchCoParticipationItemsAdapter.ViewHolder {
        return ViewHolder(AdapterResearshCoParticipationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ResearchCoParticipationItemsAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(data: MutableList<CoParticipationItemsDetails>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterResearshCoParticipationBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: CoParticipationItemsDetails) {
            with(binding) {
                textviewCodeTussFactorDetail.text = "${item.tussCod} - ${item.tussDes}"
                textviewGroupFactorDetail.isGone = isCoPartFM
                textviewDescriptionGroupFactorDetail.apply {
                    text = "${item.copartCod} - ${item.copartDes}"
                    isGone = isCoPartFM
                }
                textviewFeValue.text = item.valor.toCurrencyBRL()


            }
        }
    }

}