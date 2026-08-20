package com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.AdapterGuideAuthorizerBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.data.models.GuideAuthorizerItemsModel
import com.policlinsaude.newfeature.features.notifications.data.models.NotificationsModel
import com.policlinsaude.newfeature.utils.toDDMMYYYY

@SuppressLint("NotifyDataSetChanged", "SetTextI18n")
class GuideAuthorizerAdapter(
    private var list: MutableList<GuideAuthorizerItemsModel> = arrayListOf(),
    var setOnClickListener: (GuideAuthorizerItemsModel?) -> Unit? = { },
    var onClickCheckBox: (isRemove: Boolean, item: GuideAuthorizerItemsModel) -> Unit = { _, _ -> },
    var onItemsSelected: (isSelected: Boolean) -> Unit = {},
) : RecyclerView.Adapter<GuideAuthorizerAdapter.ViewHolder>() {

    private var isCheckBoxVisible: Boolean = false
    private var isSelectAllCheckBox: Boolean = false
    private var itemsCopy: MutableList<GuideAuthorizerItemsModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterGuideAuthorizerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemsCopy[position])
    }

    override fun getItemCount(): Int = itemsCopy.size

    fun update(data: MutableList<GuideAuthorizerItemsModel>?) {
        list = data ?: arrayListOf()
        itemsCopy = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    fun isDeleteAll() {
        list = arrayListOf()
        notifyDataSetChanged()
    }

    fun isDeleteItems(items: MutableList<GuideAuthorizerItemsModel>) {
        list.removeAll(items)
        notifyDataSetChanged()
    }

    fun isSelectAll(isSelectAll: Boolean) {
        isSelectAllCheckBox = isSelectAll
        notifyDataSetChanged()
    }

    fun isShowCheckbox(isShow: Boolean) {
        itemsCopy = if(isShow)
            list.filter { it.statusCod == "1" }.toMutableList()
        else
            list
        isCheckBoxVisible = isShow
        onItemsSelected(isShow)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: AdapterGuideAuthorizerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GuideAuthorizerItemsModel) {
            with(binding) {
                root.setOnClickListener {
                    setOnClickListener.invoke(item)
                }
                textviewNumber.text = "Número WEB: ${item.numeroWEB}"
                textviewDate.text = item.data?.toDDMMYYYY()
                textviewName.text = item.nome
                textviewStatus.apply {
                    text = item.statusDes?.trim()
                    background = GradientDrawable().apply {
                        cornerRadius = 40f
                        color = when(item.statusDes?.lowercase()) {
                            "finalizado" -> ColorStateList.valueOf(ContextCompat.getColor(context, R.color.finalizado))
                            "cancelado" -> ColorStateList.valueOf(ContextCompat.getColor(context, R.color.cancelado))
                            "em análise" -> ColorStateList.valueOf(ContextCompat.getColor(context, R.color.em_analise))
                            "aguardando resposta" -> ColorStateList.valueOf(ContextCompat.getColor(context, R.color.aguardando_resposta))
                            "respondido" -> ColorStateList.valueOf(ContextCompat.getColor(context, R.color.respondido))
                            "enviado" -> ColorStateList.valueOf(ContextCompat.getColor(context, R.color.enviado))
                            else -> ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Laranja))
                        }

                    }
                }
                guideAuthorizerCheckbox.apply {
                    isVisible = item.statusCod == "1" && isCheckBoxVisible
                    isChecked = isSelectAllCheckBox
                    setOnClickListener {
                        onClickCheckBox.invoke(!isChecked, item)
                    }
                }
            }
        }
    }

}