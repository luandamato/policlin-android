package br.com.policlinsaude.medicalGuideList.view.adapter

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.IntentHelper
import br.com.policlinsaude.databinding.CustomViewInfoMedicalGuideListBinding
import br.com.policlinsaude.databinding.ListItemMedicalGuideAndreBinding
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationMedicalGuideListPlansV4
import br.com.policlinsaude.model.PresentationQualification
import br.com.policlinsaude.qualificationInfo.QualificationAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

const val TYPE_ITEM = 0
const val TYPE_QUALIFICATION = 1

class MedicalGuideListAdapter(private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: MutableList<PresentationEstablishment> = mutableListOf()
    var listV4: MutableList<PresentationMedicalGuideListPlansV4> = mutableListOf()
    var qualifications: MutableList<PresentationQualification> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            val binding = ListItemMedicalGuideAndreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MedicalGuideListViewHolder(binding)
        } else {
            val binding = CustomViewInfoMedicalGuideListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MedicalGuideListFooterViewHolder(binding, qualifications)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MedicalGuideListViewHolder) {
            holder.format(list[position])
        } else if (holder is MedicalGuideListFooterViewHolder) {
            holder.format()
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

    fun setEstablishments(list: List<PresentationEstablishment>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun setQualificationsList(list: List<PresentationQualification>) {
        this.qualifications.clear()
        this.qualifications.addAll(list)
        notifyDataSetChanged()
    }

    fun setMedicalGuideListPlansV4(list: List<PresentationMedicalGuideListPlansV4>) {
        this.listV4.clear()
        this.listV4.addAll(list)
        notifyDataSetChanged()
    }

    class MedicalGuideListFooterViewHolder(
        private val binding: CustomViewInfoMedicalGuideListBinding,
        private val qualifications: MutableList<PresentationQualification>
    ) : RecyclerView.ViewHolder(binding.root) {

        fun format() {
            binding.recyclerView.adapter = QualificationAdapter(qualifications)
            binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)

            binding.infoTextView.setOnClickListener {
                IntentHelper.openUrlInBrowser(
                    binding.root.context,
                    binding.root.context.getString(R.string.url_custom_infos)
                )
            }

            binding.infoTextView.setText(
                Html.fromHtml(
                    binding.root.context.getString(
                        R.string.msg_information_about_icon_and_qualification_and_link
                    )
                ),
                TextView.BufferType.SPANNABLE
            )
        }
    }

    inner class MedicalGuideListViewHolder(private val binding: ListItemMedicalGuideAndreBinding) : RecyclerView.ViewHolder(binding.root) {

        fun format(establishment: PresentationEstablishment) {
            val flexBoxLayoutManager = FlexboxLayoutManager(itemView.context)
            flexBoxLayoutManager.flexDirection = FlexDirection.ROW
            flexBoxLayoutManager.flexWrap = FlexWrap.WRAP
            flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START
            binding.qualificationsRecyclerView.adapter = MedicalGuideQualificationAdapter(establishment.qualifications)
            binding.qualificationsRecyclerView.layoutManager = flexBoxLayoutManager

            binding.textViewName.text = establishment.title
            binding.textViewSubtitle.text = establishment.subTitle

            binding.textViewPlano.text = establishment.planNameV4
            binding.textViewPlano.visibility = if (establishment.showPlansV4 == 0) View.GONE else View.VISIBLE

            binding.textViewCidade.text = establishment.cityV4
            binding.textViewCidade.visibility = if (establishment.showCityV4 == 0) View.GONE else View.VISIBLE

            binding.textViewServiooTipo.text = establishment.serviceTypeV4
            binding.textViewServiooTipo.visibility = if (establishment.showServiceTypesV4 == 0) View.GONE else View.VISIBLE

            binding.textViewEspecialidade.text = establishment.especialityNameV4
            binding.textViewEspecialidade.visibility = if (establishment.showEspecialityV4 == 0) View.GONE else View.VISIBLE

            itemView.context.let {
                binding.textViewTypeEstablishment.text = it.getString(R.string.msg_type_establishment, establishment.type)
                binding.textViewSpeciality.text = it.getString(R.string.msg_speciality, establishment.speciality)
                binding.textViewAddress.text = it.getString(R.string.msg_address_format, establishment.publicPlace,
                        establishment.number, establishment.complement, establishment.neighborhood,
                        establishment.zipCode, establishment.city, establishment.state)
                binding.textViewDistance.text = if (establishment.distance == 0.0) "-" else it.getString(R.string.text_distance, establishment.distance)
            }
            binding.textViewPhoneOne.text = establishment.phoneOne
            binding.textViewPhoneTwo.text = establishment.phoneTwo
            binding.textViewPhoneOne.visibility = if (establishment.phoneOne.isEmpty()) View.GONE else View.VISIBLE
            binding.textViewPhoneTwo.visibility = if (establishment.phoneTwo.isEmpty()) View.GONE else View.VISIBLE
            binding.iconWhatsAppPhoneOne.visibility = if(establishment.typePhoneOne == "2") View.VISIBLE else View.GONE
            binding.iconWhatsAppPhoneTwo.visibility = if(establishment.typePhoneTwo == "2") View.VISIBLE else View.GONE
            binding.establishmentContainer.setOnClickListener {
                onItemClickListener.onItemClick(establishment)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(establishment: PresentationEstablishment)
    }
}
