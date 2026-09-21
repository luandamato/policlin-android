package br.com.policlinsaude.ui.fragments.notifications

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.data.models.NotificationsModel
import br.com.policlinsaude.databinding.AdapterNotificationsBinding

/**
 * Adapter da lista de notificações.
 * Mantém o mesmo comportamento do legado: checkbox de seleção, "selecionar tudo",
 * clique no item abre link/telefone.
 */
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            AdapterNotificationsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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

    fun isDeleteItems(items: List<NotificationsModel>) {
        list = list.filterNot { deleted -> items.any { it.id == deleted.id } }.toMutableList()
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

    inner class ViewHolder(private val binding: AdapterNotificationsBinding) :
        RecyclerView.ViewHolder(binding.root) {

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

                root.setOnClickListener {
                    if (item.link.isNotEmpty() && item.link != "null") {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        (binding.root.context).startActivity(intent)
                        onOpenLink.invoke(item.link)
                    }
                    if (item.phoneNumber.isNotEmpty() && item.phoneNumber != "null") {
                        onOpenPhone.invoke(item.phoneNumber)
                    }
                }
            }
        }
    }
}