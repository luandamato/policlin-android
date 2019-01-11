package br.com.policlinsaude.qualificationInfo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.model.PresentationQualification
import br.com.policlinsaude.model.PresentationQualificationForFilter
import kotlinx.android.synthetic.main.list_item_info_qualification.view.*

class QualificationFilterAdapter(private val qualifications: List<PresentationQualificationForFilter>)
    : RecyclerView.Adapter<QualificationFilterAdapter.QualificationViewHolder>() {

    override fun onBindViewHolder(holder: QualificationViewHolder, position: Int) {
        holder.format(qualifications[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QualificationViewHolder = QualificationViewHolder(LayoutInflater.from(parent.context)
          .inflate(R.layout.list_item_info_qualification, parent, false))
      //      .inflate(R.layout.custom_view_info_medical_guide_list, parent, false))

    override fun getItemCount(): Int = qualifications.size

    inner class QualificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun format(qualification: PresentationQualificationForFilter) {
            itemView.textViewDescription.text = qualification.descricao
            try {
                itemView.imageView.setImageBitmap(qualification.imgQualificacao.getBitmapFromImage())
            } catch (e: Exception) {
            }
        }
    }
}