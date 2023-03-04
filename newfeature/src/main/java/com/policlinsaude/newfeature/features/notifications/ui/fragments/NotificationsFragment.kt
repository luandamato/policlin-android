package com.policlinsaude.newfeature.features.notifications.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentNotificationsBinding
import com.policlinsaude.newfeature.features.extractor.ui.activities.FactorExtractorActivity
import com.policlinsaude.newfeature.features.notifications.ui.activities.NotificationActivity
import com.policlinsaude.newfeature.features.notifications.ui.adapters.NotificationsAdapter
import com.policlinsaude.newfeature.features.notifications.ui.viewmodels.NotificationViewModel
import com.policlinsaude.newfeature.utils.DialogHelper
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    private val viewModel by sharedViewModel<NotificationViewModel>()

    private val adapter by lazy { NotificationsAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

        (activity as NotificationActivity).showBackButton()

        with(binding) {
            recyclerViewFeItems.adapter = adapter
            adapter.onItemsSelected = { isSelected ->
                showSelectAll(isSelected)
                notificationCheckboxAll.setOnClickListener {
                    if(notificationCheckboxAll.isChecked) {
                        viewModel.addAllItems()
                    } else {
                        viewModel.clearSelectedNotifications()
                    }
                    adapter.isSelectAll(notificationCheckboxAll.isChecked)
                }

                showEditButton(!isSelected)
                showCancelButton(isSelected)

                if(!isSelected) {
                    notificationCheckboxAll.isChecked = false
                    viewModel.clearSelectedNotifications()
                }

            }

            adapter.onClickCheckBox = { isRemve, item ->
                if(isRemve)
                    viewModel.removeSelectedNotification(item)
                else
                    viewModel.addSelectedNotification(item)

            }

            buttonDeleteNotification.setOnClickListener {
                viewModel.onDeleteNotifications()
            }

            adapter.apply {
                onOpenLink = {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(it)
                    context?.startActivity(intent)
                }

                onOpenPhone = {
                    activity?.startActivity(Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:0$it")))
                }
            }
        }
    }

    private fun setupObserver() {
        with(viewModel) {
            notifications.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> {
                        showLoading()
                    }
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        it.getData()?.let { notifications ->
                            if(notifications.list.isNullOrEmpty()) {
                                DialogHelper.showErrorDialog(requireContext(), notifications.msgExterna)
                                showEditButton(false)
                            } else {
                                adapter.update(it.getData()?.list ?: arrayListOf())
                                showEditButton(true)
                            }

                        }

                    }
                    else -> {
                        hideLoading()
                    }
                }
            }


            notificationsDelete.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> {
                        showLoading()
                    }
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        it?.getData()?.let { notifications ->
                            if(notifications.codAcao == 1) {
                                if(binding.notificationCheckboxAll.isChecked) {
                                    adapter.isDeleteAll()
                                    binding.notificationCheckboxAll.isChecked = false
                                    showSelectAll(false)
                                    showCancelButton(false)
                                } else {
                                    adapter.isDeleteItems(
                                        viewModel.notificationsSelected.value ?: mutableListOf()
                                    )
                                    showSelectAll(verifyShowEditButton())
                                    showCancelButton(verifyShowEditButton())
                                }
                                notificationsSelected.value = arrayListOf()
                            }

                        }


                    }
                    else -> {
                        hideLoading()
                    }
                }
            }
        }
    }

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
        (activity as NotificationActivity).showButtonEdit(isShow) {
            //showSelectAll(isShow)
            adapter.isShowCheckbox(isShow)
        }
    }

    private fun showCancelButton(isShow: Boolean) {
        (activity as NotificationActivity).showButtonCancel(isShow) {
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

    private fun verifyShowEditButton() = !viewModel.notifications.value?.getData()?.list.isNullOrEmpty()

}