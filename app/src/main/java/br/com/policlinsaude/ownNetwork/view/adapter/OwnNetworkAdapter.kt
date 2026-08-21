package br.com.policlinsaude.ownNetwork.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.databinding.ListItemOwnNetworkBinding
import br.com.policlinsaude.medicalGuideList.view.adapter.MedicalGuideListAdapter
import br.com.policlinsaude.medicalGuideList.view.adapter.MedicalGuideQualificationAdapter
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class OwnNetworkAdapter(private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_QUALIFICATION = 1
    }

    var list: MutableList<PresentationEstablishment> = mutableListOf()
    var qualifications: MutableList<PresentationQualification> = mutableListOf()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OwnNetworkViewHolder) {
            holder.format(list[position])
        } else if (holder is MedicalGuideListAdapter.MedicalGuideListFooterViewHolder) {
            holder.format()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            OwnNetworkViewHolder(
                ListItemOwnNetworkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            MedicalGuideListAdapter.MedicalGuideListFooterViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.custom_view_info_medical_guide_list, parent, false), qualifications)
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

    inner class OwnNetworkViewHolder(private val binding: ListItemOwnNetworkBinding) : RecyclerView.ViewHolder(binding.root) {

        fun format(establishment: PresentationEstablishment) {
            val flexBoxLayoutManager = FlexboxLayoutManager(binding.root.context)
            flexBoxLayoutManager.flexDirection = FlexDirection.ROW
            flexBoxLayoutManager.flexWrap = FlexWrap.WRAP
            flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START
            binding.qualificationsRecyclerView.adapter = MedicalGuideQualificationAdapter(establishment.qualifications)
            binding.qualificationsRecyclerView.layoutManager = flexBoxLayoutManager

            binding.textViewName.text = establishment.title
            binding.textViewSubtitle.text = establishment.subTitle
            binding.root.context.let {
                binding.textViewTypeEstablishment.text = it.getString(R.string.msg_type_establishment, establishment.type)
                binding.textViewAddress.text = it.getString(R.string.msg_address_format, establishment.publicPlace,
                        establishment.number, establishment.complement, establishment.neighborhood,
                        establishment.zipCode, establishment.city, establishment.state)
            }
            binding.textViewPhoneOne.text = establishment.phoneOne
            binding.textViewPhoneTwo.text = establishment.phoneTwo
            binding.textViewPhoneOne.visibility = if (establishment.phoneOne.isEmpty()) View.GONE else View.VISIBLE
            binding.textViewPhoneTwo.visibility = if (establishment.phoneTwo.isEmpty()) View.GONE else View.VISIBLE
            binding.establishmentContainer.setOnClickListener {
                onItemClickListener.onItemClick(establishment)
            }
            try {
                binding.imageViewFront.setImageBitmap(establishment.photoFront.getBitmapFromImage())
                binding.imageViewFront.visibility = View.VISIBLE
            } catch (exception: Exception) {
                binding.imageViewFront.visibility = View.INVISIBLE
            }

        }
    }
}