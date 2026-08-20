package br.com.policlinsaude.ui.legacy.medicalGuideList.view.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.IntentHelper
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationMedicalGuideList
import br.com.policlinsaude.model.PresentationMedicalGuideListPlansV4
import br.com.policlinsaude.model.PresentationQualification
import br.com.policlinsaude.qualificationInfo.QualificationAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.list_item_medical_guide.view.*
import kotlinx.android.synthetic.main.list_item_medical_guide_andre.view.*

const val TYPE_ITEM = 0
const val TYPE_QUALIFICATION = 1

class MedicalGuideListAdapter(private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: MutableList<PresentationEstablishment> = mutableListOf()
    var listV4: MutableList<PresentationMedicalGuideListPlansV4> = mutableListOf()
    var qualifications: MutableList<PresentationQualification> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            MedicalGuideListViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.list_item_medical_guide_andre, parent, false))
        } else {
            MedicalGuideListFooterViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.custom_view_info_medical_guide_list, parent, false), qualifications)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MedicalGuideListViewHolder) {
            holder.format(list[position])
            Log.d("PRESENTATIONACTIVITY","-----------------------------------")
            Log.d("PRESENTATIONACTIVITY", "VALOR DE POSITION: " + position  )
            Log.d("PRESENTATIONACTIVITY", "VALOR DE POSITION: " + list[position].name + "TIPO DE SERVIÇO: _TIPO: " +  list[position].uType  )
            Log.d("PRESENTATIONACTIVITY","-----------------------------------")
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
        Log.d("PRESENTATIONACTIVITY","-----------------------------------")
        Log.d("PRESENTATIONACTIVITY", "TAMANHO DA LISTA NO ADAPTER NO INICIO: "+ list.size  )
        Log.d("PRESENTATIONACTIVITY","-----------------------------------")
        this.list.clear()
        Log.d("PRESENTATIONACTIVITY","-----------------------------------")
        Log.d("PRESENTATIONACTIVITY", "TAMANHO DA LISTA NO ADAPTER DEPOIS DO CLEAR: "+ list.size  )
        Log.d("PRESENTATIONACTIVITY","-----------------------------------")
        this.list.addAll(list)
        Log.d("PRESENTATIONACTIVITY","-----------------------------------")
        Log.d("PRESENTATIONACTIVITY", "TAMANHO DA LISTA NO ADAPTER DEPOIS DO ADDALL: "+ list.size  )
        Log.d("PRESENTATIONACTIVITY","-----------------------------------")
        notifyDataSetChanged()
    }

    fun setQualificationsList(list: List<PresentationQualification>) {
        Log.d("PRESENTATIONACTIVITY","-----------------------------------")
        Log.d("PRESENTATIONACTIVITY", "TAMANHO DA LISTA NO ADAPTER NO INICIO:  setQualificationsList: "+ qualifications.size  )
        Log.d("PRESENTATIONACTIVITY","-----------------------------------")
        this.qualifications.clear()
        this.qualifications.addAll(list)

        Log.d("PRESENTATIONACTIVITY","-----------------------------------")
        Log.d("PRESENTATIONACTIVITY", "TAMANHO DA LISTA NO ADAPTER NO INICIO:  setQualificationsList: "+ qualifications.size  )
        Log.d("PRESENTATIONACTIVITY","-----------------------------------")
        notifyDataSetChanged()
    }
    //Andre
    fun setMedicalGuideListPlansV4(list: List<PresentationMedicalGuideListPlansV4>) {
        this.listV4.clear()
        this.listV4.addAll(list)


        notifyDataSetChanged()
    }
//Andre -----

    class MedicalGuideListFooterViewHolder(itemView: View,
                                           private val qualifications: MutableList<PresentationQualification>)
        : RecyclerView.ViewHolder(itemView) {

        fun format() {
            val recyclerView = itemView.findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.adapter = QualificationAdapter(qualifications)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context)

            itemView.findViewById<TextView>(R.id.infoTextView).setOnClickListener { IntentHelper.openUrlInBrowser(itemView.context, itemView.context.getString(R.string.url_custom_infos)) }

            val infoTextView = itemView.findViewById<TextView>(R.id.infoTextView)
            infoTextView.setText(Html.fromHtml(itemView.context.getString(R.string.msg_information_about_icon_and_qualification_and_link)), TextView.BufferType.SPANNABLE)
        }
    }


    inner class MedicalGuideListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun format(establishment: PresentationEstablishment) {
            val flexBoxLayoutManager = FlexboxLayoutManager(itemView.context)
            flexBoxLayoutManager.flexDirection = FlexDirection.ROW
            flexBoxLayoutManager.flexWrap = FlexWrap.WRAP
            flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START
            itemView.qualificationsRecyclerView.adapter = MedicalGuideQualificationAdapter(establishment.qualifications)
                       itemView.qualificationsRecyclerView.layoutManager = flexBoxLayoutManager

            Log.d("PRESENTATIONACTIVITY","-----------------------------------")
            Log.d("PRESENTATIONACTIVITY", "establishment.showPlansV4: "+ establishment.showPlansV4  )
            Log.d("PRESENTATIONACTIVITY", "establishment.showCityV4: "+ establishment.showCityV4  )
            Log.d("PRESENTATIONACTIVITY", "establishment.showServiceTypesV4: "+ establishment.showServiceTypesV4  )
            Log.d("PRESENTATIONACTIVITY", "establishment.especialityNameV4: "+ establishment.showEspecialityV4  )

            Log.d("PRESENTATIONACTIVITY","-----------------------------------")

            itemView.textViewName.text = establishment.title
            itemView.textViewSubtitle.text = establishment.subTitle


            itemView.textViewPlano.text = establishment.planNameV4
            if (establishment.showPlansV4 == 0) itemView.textViewPlano.visibility = View.GONE
               else itemView.textViewPlano.visibility = View.VISIBLE

            itemView.textViewCidade.text = establishment.cityV4
            if (establishment.showCityV4 == 0) itemView.textViewCidade.visibility = View.GONE
            else itemView.textViewCidade.visibility = View.VISIBLE

            itemView.textViewServiooTipo.text = establishment.serviceTypeV4
            if (establishment.showServiceTypesV4 == 0) itemView.textViewServiooTipo.visibility = View.GONE
            else itemView.textViewServiooTipo.visibility = View.VISIBLE

            itemView.textViewEspecialidade.text = establishment.especialityNameV4
            if (establishment.showEspecialityV4 == 0) itemView.textViewEspecialidade.visibility = View.GONE
            else itemView.textViewEspecialidade.visibility = View.VISIBLE

            itemView.context.let {
                itemView.textViewTypeEstablishment.text = it.getString(R.string.msg_type_establishment, establishment.type)
                itemView.textViewSpeciality.text = it.getString(R.string.msg_speciality, establishment.speciality)
                itemView.textViewAddress.text = it.getString(R.string.msg_address_format, establishment.publicPlace,
                        establishment.number, establishment.complement, establishment.neighborhood,
                        establishment.zipCode, establishment.city, establishment.state)
                itemView.textViewDistance.text = if (establishment.distance == 0.0) "-" else it.getString(R.string.text_distance, establishment.distance)
            }
            itemView.textViewPhoneOne.text = establishment.phoneOne
            itemView.textViewPhoneTwo.text = establishment.phoneTwo
            itemView.textViewPhoneOne.visibility = if (establishment.phoneOne.isEmpty()) View.GONE else View.VISIBLE
            itemView.textViewPhoneTwo.visibility = if (establishment.phoneTwo.isEmpty()) View.GONE else View.VISIBLE
            itemView.iconWhatsAppPhoneOne.visibility = if(establishment.typePhoneOne == "2") View.VISIBLE else View.GONE
            itemView.iconWhatsAppPhoneTwo.visibility = if(establishment.typePhoneTwo == "2") View.VISIBLE else View.GONE
            itemView.establishmentContainer.setOnClickListener {
                onItemClickListener.onItemClick(establishment)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(establishment: PresentationEstablishment)
    }
}