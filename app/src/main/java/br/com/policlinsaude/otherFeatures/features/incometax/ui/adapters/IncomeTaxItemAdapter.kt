package com.policlinsaude.newfeature.features.incometax.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.policlinsaude.newfeature.databinding.AdapterIncomeTaxItemBinding
import com.policlinsaude.newfeature.features.incometax.data.models.IncomeTaxItemModel

class IncomeTaxItemAdapter(
    private var list: MutableList<IncomeTaxItemModel> = arrayListOf(),
    var setOnClickListener: (IncomeTaxItemModel?) -> Unit? = { },
) : RecyclerView.Adapter<IncomeTaxItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeTaxItemAdapter.ViewHolder {
        return ViewHolder(AdapterIncomeTaxItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: IncomeTaxItemAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(data: MutableList<IncomeTaxItemModel>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: AdapterIncomeTaxItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: IncomeTaxItemModel) {
            with(binding) {
                textviewYear.text = item.descricao.toString()
                //textviewDescription.text = item.descricao

                root.setOnClickListener {
                    setOnClickListener(item)
                }
            }
        }
    }

}