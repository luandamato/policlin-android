package br.com.policlinsaude.ui.activities.ownNetwork

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.PresentationEstablishment
import br.com.policlinsaude.data.models.PresentationQualification
import br.com.policlinsaude.databinding.CustomViewInfoMedicalGuideListBinding
import br.com.policlinsaude.databinding.ListItemInfoQualificationBinding
import br.com.policlinsaude.databinding.ListItemOwnNetworkBinding
import br.com.policlinsaude.databinding.ListItemQualificationIconBinding
import br.com.policlinsaude.util.extensions.getBitmapFromImage
import br.com.policlinsaude.util.extensions.openUrl
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

/**
 * Adapter de la lista de establecimientos de Rede Propia, con un ítem
 * por sucursal y un rodapie con la leyenda de calificaciones.
 *
 * Migrado de `_legacy/.../ownNetwork/view/adapter/OwnNetworkAdapter.kt`
 * (mismo formato de tarjeta: título, subtítulo, tipo, dirección, teléfonos,
 * foto de frente y click).
 */
class OwnNetworkAdapter(
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
            val binding = ListItemOwnNetworkBinding.inflate(inflater, parent, false)
            OwnNetworkViewHolder(binding)
        } else {
            val binding = CustomViewInfoMedicalGuideListBinding.inflate(inflater, parent, false)
            FooterViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OwnNetworkViewHolder) {
            holder.bind(items[position])
        } else if (holder is FooterViewHolder) {
            holder.bind(qualifications)
        }
    }

    override fun getItemCount(): Int = items.size + 1

    inner class OwnNetworkViewHolder(private val binding: ListItemOwnNetworkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(establishment: PresentationEstablishment) {
            val flexLayoutManager = FlexboxLayoutManager(binding.root.context).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.FLEX_START
            }

            binding.qualificationsRecyclerView.apply {
                adapter = QualificationIconsAdapter(establishment.qualifications)
                layoutManager = flexLayoutManager
            }

            binding.textViewName.text = establishment.title
            binding.textViewSubtitle.text = establishment.subTitle

            binding.textViewTypeEstablishment.text =
                binding.root.context.getString(
                    R.string.msg_type_establishment,
                    establishment.type
                )

            binding.textViewAddress.text =
                binding.root.context.getString(
                    R.string.msg_address_format,
                    establishment.publicPlace,
                    establishment.number,
                    establishment.complement,
                    establishment.neighborhood,
                    establishment.zipCode,
                    establishment.city,
                    establishment.state
                )

            binding.textViewPhoneOne.text = establishment.phoneOne
            binding.textViewPhoneTwo.text = establishment.phoneTwo

            binding.textViewPhoneOne.visibility =
                if (establishment.phoneOne.isEmpty()) View.GONE else View.VISIBLE

            binding.textViewPhoneTwo.visibility =
                if (establishment.phoneTwo.isEmpty()) View.GONE else View.VISIBLE

            binding.establishmentContainer.setOnClickListener {
                onItemClick(establishment)
            }

            try {
                binding.imageViewFront.setImageBitmap(establishment.photoFront.getBitmapFromImage())
                binding.imageViewFront.visibility = View.VISIBLE
            } catch (e: Exception) {
                binding.imageViewFront.visibility = View.INVISIBLE
            }
        }
    }

    inner class FooterViewHolder(private val binding: CustomViewInfoMedicalGuideListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(qualifications: List<PresentationQualification>) {
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = QualificationLegendAdapter(qualifications)
            }

            binding.infoTextView.setOnClickListener {
                binding.root.context.openUrl(
                    binding.root.context.getString(R.string.url_custom_infos)
                )
            }
        }
    }

    /** Adapter interno para los íconos de calificación dentro de cada tarjeta. */
    private class QualificationIconsAdapter(private val list: List<PresentationQualification>) :
        RecyclerView.Adapter<QualificationIconsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ListItemQualificationIconBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            try {
                holder.binding.qualificationImageView.setImageBitmap(
                    list[position].image.getBitmapFromImage()
                )
            } catch (e: Exception) {
            }
        }

        override fun getItemCount(): Int = list.size

        class ViewHolder(val binding: ListItemQualificationIconBinding) :
            RecyclerView.ViewHolder(binding.root)
    }

    /** Adapter interno para la leyenda de calificaciones en el rodapie. */
    private class QualificationLegendAdapter(private val list: List<PresentationQualification>) :
        RecyclerView.Adapter<QualificationLegendAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ListItemInfoQualificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.textViewDescription.text = list[position].description
            try {
                holder.binding.imageView.setImageBitmap(
                    list[position].image.getBitmapFromImage()
                )
            } catch (e: Exception) {
            }
        }

        override fun getItemCount(): Int = list.size

        class ViewHolder(val binding: ListItemInfoQualificationBinding) :
            RecyclerView.ViewHolder(binding.root)
    }
}