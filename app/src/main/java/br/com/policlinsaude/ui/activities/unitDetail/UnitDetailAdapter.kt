package br.com.policlinsaude.ui.activities.unitDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.PresentationEstablishment
import br.com.policlinsaude.data.models.PresentationQualification
import br.com.policlinsaude.databinding.ListItemQualificationIconBinding
import br.com.policlinsaude.databinding.ListItemUnitDetailBinding
import br.com.policlinsaude.util.extensions.getBitmapFromImage
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

/**
 * Adapter da tela de detalhe da unidade.
 *
 * Migrado de `_legacy/.../medicalGuideDetails/view/adapter/MedicalGuideDetailsAdapter.kt`
 * para o caso "Units": lista os dados (especialidade, endereço, cidade, telefones,
 * qualificações) com índice de qualificações quando aplicável.
 */
class UnitDetailAdapter(
    var onClickListenerWpp: (wpp: String) -> Unit = {}
) : RecyclerView.Adapter<UnitDetailAdapter.UnitDetailViewHolder>() {

    var list: MutableList<Pair<String, Any>> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitDetailViewHolder =
        UnitDetailViewHolder(
            ListItemUnitDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UnitDetailViewHolder, position: Int) {
        val item = list[position]
        val binding = holder.binding

        if (item.first == "Telefones" || item.first == "Telefone2") {
            binding.textViewTitle.text = item.first
            if (item.second is String) {
                val k = (item.second as String).split("@")
                if (k.isNotEmpty()) {
                    binding.qualificationsRecyclerView.visibility = View.GONE
                    binding.textViewInfo.text = k[0].replace("@", "")
                    if (k.size > 1 && k[1].replace("@", "") == "2") {
                        binding.iconWhatsAppPhoneOne.visibility = View.VISIBLE
                        binding.linearLayoutTelefone.setOnClickListener {
                            onClickListenerWpp.invoke(k[0].replace("@", ""))
                        }
                    }
                }
                if (k.size > 2) {
                    binding.qualificationsRecyclerView.visibility = View.GONE
                    binding.linearLayoutTelefone2.visibility = View.VISIBLE
                    binding.textViewInfo2.text = k[2].replace("@", "")
                    if (k.size > 3 && k[3].replace("@", "") == "2") {
                        binding.iconWhatsAppPhoneTwo.visibility = View.VISIBLE
                        binding.linearLayoutTelefone2.setOnClickListener {
                            onClickListenerWpp.invoke(k[2].replace("@", ""))
                        }
                    }
                }
            }
        } else {
            binding.textViewTitle.text = item.first
            if (item.second is String) {
                binding.qualificationsRecyclerView.visibility = View.GONE
                binding.textViewInfo.text = item.second as String
            } else {
                binding.textViewInfo.visibility = View.GONE
                val flexBoxLayoutManager = FlexboxLayoutManager(binding.root.context).apply {
                    flexDirection = FlexDirection.ROW
                    flexWrap = FlexWrap.WRAP
                    justifyContent = JustifyContent.FLEX_START
                }
                binding.qualificationsRecyclerView.visibility = View.VISIBLE
                binding.qualificationsRecyclerView.adapter =
                    QualificationIconAdapter(item.second as List<PresentationQualification>)
                binding.qualificationsRecyclerView.layoutManager = flexBoxLayoutManager
            }
        }
}

    fun setEstablishment(context: Context, establishment: PresentationEstablishment) {
        list.clear()

        if (!establishment.isOwnNetwork) {
            list.add(Pair(context.getString(R.string.title_speciality), establishment.speciality))
        }
        list.add(
            Pair(
                context.getString(R.string.title_address),
                context.getString(
                    R.string.msg_address_details_format,
                    establishment.publicPlace,
                    establishment.number,
                    establishment.complement,
                    establishment.neighborhood,
                    establishment.zipCode
                )
            )
        )
        list.add(
            Pair(
                context.getString(R.string.title_city),
                context.getString(
                    R.string.msg_city_details_format,
                    establishment.city,
                    establishment.state
                )
            )
        )
        list.add(
            Pair(
                context.getString(R.string.title_phones),
                "${establishment.phoneOne}@${establishment.typePhoneOne}@${establishment.phoneTwo}@${establishment.typePhoneTwo}"
            )
        )
        if (establishment.qualifications.isNotEmpty()) {
            list.add(Pair(context.getString(R.string.title_qualifications), establishment.qualifications))
        }
        notifyDataSetChanged()
    }

    inner class UnitDetailViewHolder(val binding: ListItemUnitDetailBinding) :
        RecyclerView.ViewHolder(binding.root)

    private class QualificationIconAdapter(
        private val list: List<PresentationQualification>
    ) : RecyclerView.Adapter<QualificationIconAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                ListItemQualificationIconBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            try {
                holder.binding.qualificationImageView.setImageBitmap(
                    list[position].image.getBitmapFromImage()
                )
            } catch (e: Exception) {
                // mantém ícone padrão
            }
        }

        override fun getItemCount(): Int = list.size

        class ViewHolder(val binding: ListItemQualificationIconBinding) :
            RecyclerView.ViewHolder(binding.root)
    }
}