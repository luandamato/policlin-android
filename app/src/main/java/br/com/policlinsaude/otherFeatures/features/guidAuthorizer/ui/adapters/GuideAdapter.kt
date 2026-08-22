package com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.databinding.AdapterGuideBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideItemsModel
import com.policlinsaude.newfeature.utils.toDDMMYYYY

@SuppressLint("NotifyDataSetChanged", "SetTextI18n")
class GuideAdapter(
    private var list: MutableList<GuideItemsModel> = arrayListOf(),
    var setOnClickListener: (GuideItemsModel?) -> Unit? = { },
) : RecyclerView.Adapter<GuideAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun update(data: MutableList<GuideItemsModel>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterGuideBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GuideItemsModel) {
            with(binding) {
                textviewName.text = item.nome
                textviewDate.text = item.data?.toDDMMYYYY()
                root.setOnClickListener {
                    setOnClickListener(item)
                }
            }
        }
    }

}