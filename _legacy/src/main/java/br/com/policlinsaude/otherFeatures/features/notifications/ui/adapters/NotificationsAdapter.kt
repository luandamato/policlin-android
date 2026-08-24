package com.policlinsaude.newfeature.features.notifications.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.databinding.AdapterNotificationsBinding
import com.policlinsaude.newfeature.features.notifications.data.models.NotificationsModel
import com.policlinsaude.newfeature.features.notifications.ui.viewmodels.NotificationViewModel

@SuppressLint("NotifyDataSetChanged")
class NotificationsAdapter(
    private var list: MutableList<NotificationsModel> = arrayListOf(),
    var onClickCheckBox: (isRemove: Boolean, item: NotificationsModel) -> Unit = { _, _ -> },
    var onItemsSelected: (isSelected: Boolean) -> Unit = {},
    var onOpenLink: (link: String) -> Unit = {},
    var onOpenPhone: (phone: String) -> Unit = {}
) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    private var isCheckBoxVisible: Boolean = false
    private var isSelectAllCheckBox: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsAdapter.ViewHolder {
        return ViewHolder(AdapterNotificationsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NotificationsAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size


    fun update(data: MutableList<NotificationsModel>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    fun isDeleteAll() {
        list = arrayListOf()
        notifyDataSetChanged()
    }

    fun isDeleteItems(items: MutableList<NotificationsModel>) {
        list.removeAll(items)
        notifyDataSetChanged()
    }

    fun isSelectAll(isSelectAll: Boolean) {
        isSelectAllCheckBox = isSelectAll
        notifyDataSetChanged()
    }

    fun isShowCheckbox(isShow: Boolean) {
        isCheckBoxVisible = isShow
        onItemsSelected(isShow)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterNotificationsBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: NotificationsModel) {
            with(binding) {
                notificationDescription.text = item.description
                notificationTitle.text = item.title
                notificationDescriptionHour.text = item.sendDate
                notificationCheckbox.apply {
                    isVisible = isCheckBoxVisible
                    isChecked = isSelectAllCheckBox
                    setOnClickListener {
                        onClickCheckBox.invoke(!isChecked, item)
                    }
                }

                root.apply {

                    setOnClickListener {
                        if(item.link.isNotEmpty()) {
                            onOpenLink.invoke(item.link)
                        }

                        if(item.phoneNumber.isNotEmpty()) {
                            onOpenPhone.invoke(item.phoneNumber)
                        }
                    }
                }


            }
        }
    }

}