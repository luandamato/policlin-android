package br.com.policlinsaude.qualificationInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.databinding.ListItemInfoQualificationBinding
import br.com.policlinsaude.model.PresentationQualificationForFilter

class QualificationFilterAdapter(private val qualifications: List<PresentationQualificationForFilter>) :
    RecyclerView.Adapter<QualificationFilterAdapter.QualificationViewHolder>() {

    override fun onBindViewHolder(holder: QualificationViewHolder, position: Int) {
        holder.bind(qualifications[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QualificationViewHolder {
        val binding = ListItemInfoQualificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QualificationViewHolder(binding)
    }

    override fun getItemCount(): Int = qualifications.size

    inner class QualificationViewHolder(private val binding: ListItemInfoQualificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(qualification: PresentationQualificationForFilter) {
            binding.textViewDescription.text = qualification.descricao
            try {
                binding.imageView.setImageBitmap(qualification.imgQualificacao.getBitmapFromImage())
            } catch (e: Exception) {
            }
        }
    }
}
