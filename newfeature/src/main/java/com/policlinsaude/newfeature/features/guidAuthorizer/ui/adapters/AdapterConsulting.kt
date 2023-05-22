package com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.policlinsaude.newfeature.databinding.AdapterConsultingBinding
import com.policlinsaude.newfeature.databinding.AdapterPictureBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.DeadlinesItemsModel

@SuppressLint("NotifyDataSetChanged")
class AdapterConsulting(
    var list: MutableList<DeadlinesItemsModel>? = arrayListOf(),
) : RecyclerView.Adapter<AdapterConsulting.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterConsultingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list?.get(position))
    }

    override fun getItemCount(): Int = list?.size ?: 0


    fun update(data: MutableList<DeadlinesItemsModel>?) {
        list = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterConsultingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DeadlinesItemsModel?) {
            with(binding) {
                textviewTitle.text = item?.servico
                textviewDeadline.text = item?.prazo
            }
        }
    }

}