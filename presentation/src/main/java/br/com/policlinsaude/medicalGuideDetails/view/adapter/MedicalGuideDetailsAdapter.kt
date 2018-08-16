package br.com.policlinsaude.medicalGuideDetails.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.home.view.adapter.HomeViewHolder
import br.com.policlinsaude.medicalGuideList.view.adapter.MedicalGuideQualificationAdapter
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.list_item_medical_guide_details.view.*

class MedicalGuideDetailsAdapter : RecyclerView.Adapter<HomeViewHolder>() {

    var list: MutableList<Pair<String, Any>> = mutableListOf()

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = list[position]
        holder.itemView.textViewTitle.text = item.first
        if (item.second is String) {
            holder.itemView.qualificationsRecyclerView.visibility = View.GONE
            holder.itemView.textViewInfo.text = item.second as String
        } else {
            holder.itemView.textViewInfo.visibility = View.GONE
            val flexBoxLayoutManager = FlexboxLayoutManager(holder.itemView.context)
            flexBoxLayoutManager.flexDirection = FlexDirection.ROW
            flexBoxLayoutManager.flexWrap = FlexWrap.WRAP
            flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START
            holder.itemView.qualificationsRecyclerView.adapter = MedicalGuideQualificationAdapter(item.second as List<PresentationQualification>)
            holder.itemView.qualificationsRecyclerView.layoutManager = flexBoxLayoutManager
        }
    }

    fun setPresentationEstablishment(context: Context,
                                     presentationEstablishment: PresentationEstablishment, caller: String) {
        if (caller != "Units") {
            list.add(Pair(context.getString(R.string.title_social_name),
                    presentationEstablishment.socialName))
            list.add(Pair(context.getString(R.string.title_cnpj).toUpperCase(),
                    presentationEstablishment.cnpj))
            list.add(Pair(context.getString(R.string.title_type_establishment),
                    presentationEstablishment.type))
        }

        if (!presentationEstablishment.isOwnNetwork) {
            list.add(Pair(context.getString(R.string.title_speciality),
                    presentationEstablishment.speciality))
        }
        list.add(Pair(context.getString(R.string.title_address),
                context.getString(R.string.msg_address_details_format, presentationEstablishment.publicPlace,
                        presentationEstablishment.number, presentationEstablishment.complement,
                        presentationEstablishment.neighborhood, presentationEstablishment.zipCode)))
        list.add(Pair(context.getString(R.string.title_city),
                context.getString(R.string.msg_city_details_format, presentationEstablishment.city,
                        presentationEstablishment.state)))
        list.add(Pair(context.getString(R.string.title_phones),
                "${presentationEstablishment.phoneOne}\n${presentationEstablishment.phoneTwo}"))
        if (presentationEstablishment.qualifications.isNotEmpty()) {
            list.add(Pair(context.getString(R.string.title_qualifications), presentationEstablishment.qualifications))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder = HomeViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_medical_guide_details, parent, false))


    override fun getItemCount(): Int = list.size

}