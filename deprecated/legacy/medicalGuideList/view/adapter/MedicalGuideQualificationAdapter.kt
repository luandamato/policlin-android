package br.com.policlinsaude.ui.legacy.medicalGuideList.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.model.PresentationQualification
import kotlinx.android.synthetic.main.list_item_qualification_icon.view.*

/**
 * Created by lmiyagi on 3/22/18.
 */
class MedicalGuideQualificationAdapter(private val qualifications: List<PresentationQualification>) : RecyclerView.Adapter<MedicalGuideQualificationAdapter.MedicalGuideQualificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalGuideQualificationViewHolder {
        return MedicalGuideQualificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_qualification_icon, parent, false))
    }

    override fun getItemCount(): Int = qualifications.size

    override fun onBindViewHolder(holder: MedicalGuideQualificationViewHolder, position: Int) {
        holder.format(qualifications[position])
    }

    inner class MedicalGuideQualificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun format(presentationQualification: PresentationQualification) {
            itemView.qualificationImageView.setImageBitmap(presentationQualification.image.getBitmapFromImage())
        }
    }
}