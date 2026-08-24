package br.com.policlinsaude.units.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.databinding.CustomViewInfoMedicalGuideListBinding
import br.com.policlinsaude.databinding.ListItemUnitsBinding
import br.com.policlinsaude.medicalGuideList.view.adapter.MedicalGuideListAdapter
import br.com.policlinsaude.medicalGuideList.view.adapter.MedicalGuideQualificationAdapter
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class UnitsAdapter(private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_QUALIFICATION = 1
    }

    private val list = mutableListOf<PresentationEstablishment>()
    private val qualifications = mutableListOf<PresentationQualification>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return when (viewType) {

            TYPE_ITEM -> {
                UnitsViewHolder(
                    ListItemUnitsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            TYPE_QUALIFICATION -> {
                val binding = CustomViewInfoMedicalGuideListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                MedicalGuideListAdapter.MedicalGuideListFooterViewHolder(
                    binding,
                    qualifications
                )
            }

            else -> {
                throw IllegalArgumentException("ViewType desconhecido: $viewType")
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {
            is UnitsViewHolder -> {
                holder.format(list[position])
            }

            is MedicalGuideListAdapter.MedicalGuideListFooterViewHolder -> {
                holder.format()
            }
        }
    }

    override fun getItemCount(): Int = list.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position < list.size) {
            TYPE_ITEM
        } else {
            TYPE_QUALIFICATION
        }
    }

    fun setEstablishments(
        establishments: MutableList<PresentationEstablishment>,
        qualifications: MutableList<PresentationQualification>
    ) {
        list.clear()
        list.addAll(establishments)

        this.qualifications.clear()
        this.qualifications.addAll(qualifications)

        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(establishment: PresentationEstablishment)
    }

    inner class UnitsViewHolder(
        private val binding: ListItemUnitsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun format(establishment: PresentationEstablishment) {

            val flexBoxLayoutManager = FlexboxLayoutManager(
                binding.root.context
            ).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.FLEX_START
            }

            binding.qualificationsRecyclerViewUnits.apply {
                adapter = MedicalGuideQualificationAdapter(
                    establishment.qualifications
                )
                layoutManager = flexBoxLayoutManager
            }

            binding.textViewNameUnits.text = establishment.title
            binding.textViewSubtitleUnits.text = establishment.subTitle

            binding.textViewTypeEstablishmentUnits.apply {
                text = binding.root.context.getString(
                    R.string.msg_type_establishment,
                    establishment.type
                )
                visibility = View.GONE
            }

            binding.textViewAddressUnits.text =
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

            binding.textViewPhoneOneUnits.apply {
                text = establishment.phoneOne
                visibility =
                    if (establishment.phoneOne.isEmpty()) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
            }

            binding.textViewPhoneTwoUnits.apply {
                text = establishment.phoneTwo
                visibility =
                    if (establishment.phoneTwo.isEmpty()) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
            }

            binding.establishmentContainerUnits.setOnClickListener {
                onItemClickListener.onItemClick(establishment)
            }

            try {
                binding.imageViewFrontUnits.setImageBitmap(
                    establishment.photoFront.getBitmapFromImage()
                )
                binding.imageViewFrontUnits.visibility = View.VISIBLE
            } catch (exception: Exception) {
                binding.imageViewFrontUnits.visibility = View.INVISIBLE
            }
        }
    }
}