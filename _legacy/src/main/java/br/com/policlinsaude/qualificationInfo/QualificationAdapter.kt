package br.com.policlinsaude.qualificationInfo

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.databinding.ListItemInfoQualificationBinding
import br.com.policlinsaude.model.PresentationQualification

class QualificationAdapter(private val qualifications: List<PresentationQualification>)
    : RecyclerView.Adapter<QualificationAdapter.QualificationViewHolder>() {

    override fun onBindViewHolder(holder: QualificationViewHolder, position: Int) {
        holder.format(qualifications[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QualificationViewHolder = QualificationViewHolder(
        ListItemInfoQualificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun getItemCount(): Int = qualifications.size

    inner class QualificationViewHolder(private val binding: ListItemInfoQualificationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun format(qualification: PresentationQualification) {
            binding.textViewDescription.text = qualification.description
            try {
                binding.imageView.setImageBitmap(qualification.image.getBitmapFromImage())
            } catch (e: Exception) {
            }
        }
    }
}