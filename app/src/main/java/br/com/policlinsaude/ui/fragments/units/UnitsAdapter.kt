package br.com.policlinsaude.ui.fragments.units

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.PresentationEstablishment
import br.com.policlinsaude.data.models.PresentationQualification
import br.com.policlinsaude.databinding.CustomViewInfoMedicalGuideListBinding
import br.com.policlinsaude.databinding.ListItemUnitsBinding
import br.com.policlinsaude.databinding.ListItemQualificationIconBinding
import br.com.policlinsaude.databinding.ListItemInfoQualificationBinding
import br.com.policlinsaude.util.extensions.getBitmapFromImage
import br.com.policlinsaude.util.extensions.openUrl
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

/**
 * Adapter para a listagem de Unidades (Rede Própria).
 * Inclui suporte a itens de estabelecimento e um rodapé com a legenda de qualificações.
 */
class UnitsAdapter(
    private val onItemClick: (PresentationEstablishment) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<PresentationEstablishment>()
    private val qualifications = mutableListOf<PresentationQualification>()

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_FOOTER = 1
    }

    fun update(newItems: List<PresentationEstablishment>, newQualifications: List<PresentationQualification>) {
        items.clear()
        items.addAll(newItems)
        qualifications.clear()
        qualifications.addAll(newQualifications)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < items.size) TYPE_ITEM else TYPE_FOOTER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_ITEM) {
            val binding = ListItemUnitsBinding.inflate(inflater, parent, false)
            UnitsViewHolder(binding)
        } else {
            val binding = CustomViewInfoMedicalGuideListBinding.inflate(inflater, parent, false)
            FooterViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UnitsViewHolder) {
            holder.bind(items[position])
        } else if (holder is FooterViewHolder) {
            holder.bind(qualifications)
        }
    }

    override fun getItemCount(): Int = items.size + 1

    inner class UnitsViewHolder(private val binding: ListItemUnitsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(establishment: PresentationEstablishment) {
            binding.textViewNameUnits.text = establishment.title
            binding.textViewSubtitleUnits.text = establishment.subTitle
            binding.textViewAddressUnits.text = establishment.getFullAddress()

            binding.textViewPhoneOneUnits.apply {
                text = establishment.phoneOne
                visibility = if (establishment.phoneOne.isEmpty()) View.GONE else View.VISIBLE
            }

            binding.textViewPhoneTwoUnits.apply {
                text = establishment.phoneTwo
                visibility = if (establishment.phoneTwo.isEmpty()) View.GONE else View.VISIBLE
            }

            // Configuração do Flexbox para ícones de qualificação
            val flexManager = FlexboxLayoutManager(binding.root.context).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.FLEX_START
            }
            binding.qualificationsRecyclerViewUnits.apply {
                layoutManager = flexManager
                adapter = QualificationIconsAdapter(establishment.qualifications)
            }

            binding.establishmentContainerUnits.setOnClickListener { onItemClick(establishment) }

            try {
                if (establishment.photoFront.isNotEmpty()) {
                    binding.imageViewFrontUnits.setImageBitmap(establishment.photoFront.getBitmapFromImage())
                    binding.imageViewFrontUnits.visibility = View.VISIBLE
                } else {
                    binding.imageViewFrontUnits.visibility = View.GONE
                }
            } catch (e: Exception) {
                binding.imageViewFrontUnits.visibility = View.GONE
            }
        }
    }

    inner class FooterViewHolder(private val binding: CustomViewInfoMedicalGuideListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(qualifications: List<PresentationQualification>) {
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = QualificationLegendAdapter(qualifications)
            }
            
            binding.infoTextView.setOnClickListener {
                it.context.openUrl(it.context.getString(R.string.url_custom_infos))
            }
        }
    }

    /** Adapter interno para os ícones de qualificação dentro de cada card. */
    private class QualificationIconsAdapter(private val list: List<PresentationQualification>) :
        RecyclerView.Adapter<QualificationIconsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ListItemQualificationIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            try {
                holder.binding.qualificationImageView.setImageBitmap(list[position].image.getBitmapFromImage())
            } catch (e: Exception) { }
        }

        override fun getItemCount(): Int = list.size

        class ViewHolder(val binding: ListItemQualificationIconBinding) : RecyclerView.ViewHolder(binding.root)
    }

    /** Adapter interno para a legenda de qualificações no rodapé. */
    private class QualificationLegendAdapter(private val list: List<PresentationQualification>) :
        RecyclerView.Adapter<QualificationLegendAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ListItemInfoQualificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.textViewDescription.text = list[position].description
            try {
                holder.binding.imageView.setImageBitmap(list[position].image.getBitmapFromImage())
            } catch (e: Exception) { }
        }

        override fun getItemCount(): Int = list.size

        class ViewHolder(val binding: ListItemInfoQualificationBinding) : RecyclerView.ViewHolder(binding.root)
    }
}
