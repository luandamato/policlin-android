package br.com.policlinsaude.ui.fragments.coparticipation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.data.models.CoParticipationItemsDetails
import br.com.policlinsaude.databinding.AdapterResearshCoParticipationBinding
import br.com.policlinsaude.util.extensions.toCurrencyBRL

/**
 * Adapter dos itens da Pesquisa de Valores de Coparticipação.
 *
 * Migrado de `_legacy/.../coparticipation/ui/adapters/ResearchCoParticipationItemsAdapter.kt`
 * (mesmo layout e regras: esconde grupo/descrição quando `isCoPartFm`).
 */
class ResearchCoParticipationItemsAdapter(
    private val isCoPartFM: Boolean,
    private var list: MutableList<CoParticipationItemsDetails> = arrayListOf(),
) : RecyclerView.Adapter<ResearchCoParticipationItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            AdapterResearshCoParticipationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(data: MutableList<CoParticipationItemsDetails>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterResearshCoParticipationBinding) :
        RecyclerView.ViewHolder(binding.root) {

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
