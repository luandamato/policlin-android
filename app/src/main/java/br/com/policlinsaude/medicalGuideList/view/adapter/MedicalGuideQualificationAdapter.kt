package br.com.policlinsaude.medicalGuideList.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.databinding.ListItemQualificationIconBinding
import br.com.policlinsaude.model.PresentationQualification

class MedicalGuideQualificationAdapter(private val qualifications: List<PresentationQualification>) :
    RecyclerView.Adapter<MedicalGuideQualificationAdapter.MedicalGuideQualificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalGuideQualificationViewHolder {
        val binding = ListItemQualificationIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MedicalGuideQualificationViewHolder(binding)
    }

    override fun getItemCount(): Int = qualifications.size

    override fun onBindViewHolder(holder: MedicalGuideQualificationViewHolder, position: Int) {
        holder.bind(qualifications[position])
    }

    inner class MedicalGuideQualificationViewHolder(private val binding: ListItemQualificationIconBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(presentationQualification: PresentationQualification) {
            binding.qualificationImageView.setImageBitmap(presentationQualification.image.getBitmapFromImage())
        }
    }
}
