package br.com.policlinsaude.ui.fragments.notifications

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import br.com.policlinsaude.data.models.NotificationsModel
import br.com.policlinsaude.databinding.FragmentNotificationsBinding
import br.com.policlinsaude.ui.activities.notifications.NotificationActivity
import br.com.policlinsaude.ui.dialogs.DialogHelper
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Lista de notificações (MVVM).
 *
 * Migrado do legado `NotificationsFragment` — sem Presenter/Navigator; estado
 * no [NotificationViewModel], seleção/edição/deleção na UI (mesmo padrão).
 */
class NotificationsFragment : Fragment() {

    companion object {
        fun newInstance(): NotificationsFragment = NotificationsFragment()
    }

    private lateinit var binding: FragmentNotificationsBinding

    private val viewModel: NotificationViewModel by viewModel()

    private var items: MutableList<NotificationsModel> = arrayListOf()

    private lateinit var adapter: NotificationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onGetNotifications()
        setupViews()
        setupObserver()
    }

    private fun setupViews() {
        (activity as? NotificationActivity)?.showBackButton()

        adapter = NotificationsAdapter(
            onClickCheckBox = { isRemove, item ->
                if (isRemove) viewModel.removeSelectedNotification(item)
                else viewModel.addSelectedNotification(item)
            },
            onItemsSelected = { isSelected ->
                showSelectAll(isSelected)
                binding.notificationCheckboxAll.setOnClickListener {
                    if (binding.notificationCheckboxAll.isChecked) {
                        viewModel.addAllItems(items)
                    } else {
                        viewModel.clearSelectedNotifications()
                    }
                    adapter.isSelectAll(binding.notificationCheckboxAll.isChecked)
                }
                showEditButton(!isSelected)
                showCancelButton(isSelected)

                if (!isSelected) {
                    binding.notificationCheckboxAll.isChecked = false
                    viewModel.clearSelectedNotifications()
                }
            },
            onOpenLink = { link ->
                context?.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(link))
                )
            },
            onOpenPhone = { phone ->
                context?.startActivity(
                    Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:0$phone"))
                )
            }
        )

        with(binding) {
            recyclerViewFeItems.adapter = adapter

            buttonDeleteNotification.setOnClickListener {
                viewModel.onDeleteNotifications()
            }
        }
    }

    private fun setupObserver() {
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) showLoading() else hideLoading()
        }

        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                NotificationEvent.Nothing -> Unit
                is NotificationEvent.NotificationsLoaded -> {
                    items = event.list.toMutableList()
                    adapter.update(items)
                    showEditButton(true)
                }
                is NotificationEvent.ShowError -> {
                    hideLoading()
                    DialogHelper.showErrorDialog(requireContext(), event.message)
                    showEditButton(false)
                }
                NotificationEvent.DeleteSuccess -> {
                    hideLoading()
                    if (binding.notificationCheckboxAll.isChecked) {
                        items = arrayListOf()
                        adapter.isDeleteAll()
                        binding.notificationCheckboxAll.isChecked = false
                        showSelectAll(false)
                        showCancelButton(false)
                    } else {
                        adapter.isDeleteItems(viewModel.notificationsSelected.value ?: emptyList())
                        showSelectAll(verifyShowEditButton())
                        showCancelButton(verifyShowEditButton())
                    }
                    viewModel.clearSelectedNotifications()
                }
            }
        }
    }

    // =====================================================================
    // Helpers visuais
    // =====================================================================
    private fun showLoading() {
        with(binding) {
            linearLayoutItems.alpha = .1F
            progressBarNotifications.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
            linearLayoutItems.alpha = 1F
            progressBarNotifications.visibility = View.GONE
        }
    }

    private fun showSelectAll(isShow: Boolean) {
        with(binding) {
            llCheckboxAll.isVisible = isShow
            viewCheckboxAll.isVisible = isShow
            buttonDeleteNotification.isVisible = isShow
        }
    }

    private fun showEditButton(isShow: Boolean) {
        (activity as? NotificationActivity)?.showButtonEdit(isShow) {
            adapter.isShowCheckbox(isShow)
        }
    }

    private fun showCancelButton(isShow: Boolean) {
        (activity as? NotificationActivity)?.showButtonCancel(isShow) {
            viewModel.clearSelectedNotifications()
            adapter.apply {
                isShowCheckbox(false)
                isSelectAll(false)
            }
            binding.notificationCheckboxAll.isChecked = false
            showSelectAll(false)
            showEditButton(verifyShowEditButton())
        }
    }

    private fun verifyShowEditButton(): Boolean = items.isNotEmpty()
}