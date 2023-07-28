package com.policlinsaude.newfeature.features.Token.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.policlinsaude.newfeature.databinding.AdapterDependenteBinding
import com.policlinsaude.newfeature.features.Token.models.BeneficiarioModel
import com.policlinsaude.newfeature.utils.toDDMMYYYY

@SuppressLint("NotifyDataSetChanged", "SetTextI18n")
class DependetAdapter(
    private var list: MutableList<BeneficiarioModel> = arrayListOf(),
    var setOnClickListener: (BeneficiarioModel?) -> Unit? = { },
) : RecyclerView.Adapter<DependetAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterDependenteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun update(data: MutableList<BeneficiarioModel>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterDependenteBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BeneficiarioModel) {
            with(binding) {
                txtNome.text = item.Nome_Beneficiario
                txtMatricula.text = "${item.matricula}-${item.ordem}"
                root.setOnClickListener {
                    setOnClickListener(item)
                }
            }
        }
    }

}