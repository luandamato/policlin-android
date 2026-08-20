package br.com.policlinsaude.qualificationInfo

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.model.PresentationQualification
import kotlinx.android.synthetic.main.list_item_info_qualification.view.*

class QualificationAdapter(private val qualifications: List<PresentationQualification>)
    : RecyclerView.Adapter<QualificationAdapter.QualificationViewHolder>() {

    override fun onBindViewHolder(holder: QualificationViewHolder, position: Int) {
        holder.format(qualifications[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QualificationViewHolder = QualificationViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_info_qualification, parent, false))


    override fun getItemCount(): Int = qualifications.size

    inner class QualificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun format(qualification: PresentationQualification) {
            itemView.textViewDescription.text = qualification.description
            try {
                itemView.imageView.setImageBitmap(qualification.image.getBitmapFromImage())
            } catch (e: Exception) {
            }
        }
    }
}