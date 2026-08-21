package br.com.policlinsaude.units.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.getBitmapFromImage
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

    var list: MutableList<PresentationEstablishment> = mutableListOf()
    var qualifications: MutableList<PresentationQualification> = mutableListOf()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UnitsViewHolder) {
            holder.format(list[position])
        } else if (holder is MedicalGuideListAdapter.MedicalGuideListFooterViewHolder) {
            holder.format()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            UnitsViewHolder(
                ListItemUnitsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            MedicalGuideListAdapter.MedicalGuideListFooterViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.custom_view_info_medical_guide_list_units, parent, false), qualifications)
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

    fun setEstablishments(establishments: MutableList<PresentationEstablishment>, qualifications: MutableList<PresentationQualification>) {
        this.list.clear()
        this.list.addAll(establishments)
        this.qualifications.clear()
        this.qualifications.addAll(qualifications)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(establishment: PresentationEstablishment)
    }

    inner class UnitsViewHolder(private val binding: ListItemUnitsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun format(establishment: PresentationEstablishment) {
            val flexBoxLayoutManager = FlexboxLayoutManager(binding.root.context)
            flexBoxLayoutManager.flexDirection = FlexDirection.ROW
            flexBoxLayoutManager.flexWrap = FlexWrap.WRAP
            flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START
            binding.qualificationsRecyclerViewUnits.adapter = MedicalGuideQualificationAdapter(establishment.qualifications)
            binding.qualificationsRecyclerViewUnits.layoutManager = flexBoxLayoutManager

            binding.textViewNameUnits.text = establishment.title
            binding.textViewSubtitleUnits.text = establishment.subTitle
            binding.root.context.let {
                binding.textViewTypeEstablishmentUnits.text = it.getString(R.string.msg_type_establishment, establishment.type)
                binding.textViewTypeEstablishmentUnits.visibility = View.GONE
                binding.textViewAddressUnits.text = it.getString(R.string.msg_address_format, establishment.publicPlace,
                        establishment.number, establishment.complement, establishment.neighborhood,
                        establishment.zipCode, establishment.city, establishment.state)
            }
            binding.textViewPhoneOneUnits.text = establishment.phoneOne
            binding.textViewPhoneTwoUnits.text = establishment.phoneTwo
            binding.textViewPhoneOneUnits.visibility = if (establishment.phoneOne.isEmpty()) View.GONE else View.VISIBLE
            binding.textViewPhoneTwoUnits.visibility = if (establishment.phoneTwo.isEmpty()) View.GONE else View.VISIBLE
            binding.establishmentContainerUnits.setOnClickListener {
                onItemClickListener.onItemClick(establishment)
            }
            try {
                binding.imageViewFrontUnits.setImageBitmap(establishment.photoFront.getBitmapFromImage())
                binding.imageViewFrontUnits.visibility = View.VISIBLE
            } catch (exception: Exception) {
                binding.imageViewFrontUnits.visibility = View.INVISIBLE
            }
        }
    }
}