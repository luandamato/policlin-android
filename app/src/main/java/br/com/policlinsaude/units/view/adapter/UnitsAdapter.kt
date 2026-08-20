package br.com.policlinsaude.units.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.medicalGuideList.view.adapter.MedicalGuideListAdapter
import br.com.policlinsaude.medicalGuideList.view.adapter.MedicalGuideQualificationAdapter
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.list_item_own_network.view.*
import kotlinx.android.synthetic.main.list_item_units.view.*

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
            UnitsViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_units, parent, false))
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

    inner class UnitsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun format(establishment: PresentationEstablishment) {
            val flexBoxLayoutManager = FlexboxLayoutManager(itemView.context)
            flexBoxLayoutManager.flexDirection = FlexDirection.ROW
            flexBoxLayoutManager.flexWrap = FlexWrap.WRAP
            flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START
            itemView.qualificationsRecyclerViewUnits.adapter = MedicalGuideQualificationAdapter(establishment.qualifications)
            itemView.qualificationsRecyclerViewUnits.layoutManager = flexBoxLayoutManager

            itemView.textViewNameUnits.text = establishment.title
            itemView.textViewSubtitleUnits.text = establishment.subTitle
            itemView.context.let {
                itemView.textViewTypeEstablishmentUnits.text = it.getString(R.string.msg_type_establishment, establishment.type)
                itemView.textViewTypeEstablishmentUnits.visibility = View.GONE
                itemView.textViewAddressUnits.text = it.getString(R.string.msg_address_format, establishment.publicPlace,
                        establishment.number, establishment.complement, establishment.neighborhood,
                        establishment.zipCode, establishment.city, establishment.state)
            }
            itemView.textViewPhoneOneUnits.text = establishment.phoneOne
            itemView.textViewPhoneTwoUnits.text = establishment.phoneTwo
            itemView.textViewPhoneOneUnits.visibility = if (establishment.phoneOne.isEmpty()) View.GONE else View.VISIBLE
            itemView.textViewPhoneTwoUnits.visibility = if (establishment.phoneTwo.isEmpty()) View.GONE else View.VISIBLE
            itemView.establishmentContainerUnits.setOnClickListener {
                onItemClickListener.onItemClick(establishment)
            }
            try {
                itemView.imageViewFrontUnits.setImageBitmap(establishment.photoFront.getBitmapFromImage())
                itemView.imageViewFrontUnits.visibility = View.VISIBLE
            } catch (exception: Exception) {
                itemView.imageViewFront.visibility = View.INVISIBLE
            }
        }
    }
}