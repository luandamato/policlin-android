package br.com.policlinsaude.medicalGuideDetails.view.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.android.synthetic.main.activity_medical_guide_details.*
import kotlinx.android.synthetic.main.list_item_medical_guide_details.view.*

class MedicalGuideDetailsAdapter(
    var onClickListenerWpp: (wpp: String) -> Unit ={}
) : RecyclerView.Adapter<HomeViewHolder>() {

    var list: MutableList<Pair<String, Any>> = mutableListOf()

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = list[position]

        if(item.first == "Telefones" || item.first == "Telefone2") {
            holder.itemView.textViewTitle.text = item.first
            if (item.second is String) {
                val k = (item.second as String).split("@")
                if(k.isNotEmpty()) {
                    holder.itemView.qualificationsRecyclerView.visibility = View.GONE
                    holder.itemView.textViewInfo.text = k[0].replace("@", "")
                    Log.d("Te123", k[1])
                    if(k[1].replace("@", "") == "2") {
                        Log.d("Te123", (k[1].replace("@", "") == "2").toString())
                        holder.itemView.iconWhatsAppPhoneOne.visibility = View.VISIBLE
                        holder.itemView.linearLayout_telefone.setOnClickListener {
                            Log.d("T >>>> ", (k[1].replace("@", "") == "2").toString())
                            onClickListenerWpp.invoke(k[0].replace("@", ""))
                        }
                    }
                }
                if(k.size > 2) {
                    holder.itemView.qualificationsRecyclerView.visibility = View.GONE
                    holder.itemView.linearLayout_telefone2.visibility = View.VISIBLE
                    holder.itemView.textViewInfo2.text = k[2].replace("@", "")
                    if(k[3].replace("@", "") == "2") {
                        holder.itemView.iconWhatsAppPhoneTwo.visibility = View.VISIBLE
                        holder.itemView.linearLayout_telefone2.setOnClickListener {
                            Log.d("Te >>>> ", (k[1].replace("@", "") == "2").toString())
                            onClickListenerWpp.invoke(k[2].replace("@", ""))
                        }
                    }

                }

            }
        } else {
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

    }

    fun setPresentationEstablishment(context: Context, presentationEstablishment: PresentationEstablishment, caller: String) {
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
            "${presentationEstablishment.phoneOne}@${presentationEstablishment.typePhoneOne}@${presentationEstablishment.phoneTwo}@${presentationEstablishment.typePhoneTwo}"))
        if (presentationEstablishment.qualifications.isNotEmpty()) {
            list.add(Pair(context.getString(R.string.title_qualifications), presentationEstablishment.qualifications))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder = HomeViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_medical_guide_details, parent, false))


    override fun getItemCount(): Int = list.size

}