package com.policlinsaude.newfeature.features.deleteUser.ui.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentDeleteUserBinding
import com.policlinsaude.newfeature.features.deleteUser.ui.ViewModel.DeleteUserViewModel
import com.policlinsaude.newfeature.features.deleteUser.ui.Activity.DeleteUserActivity
import com.policlinsaude.newfeature.utils.DialogHelper
import com.policlinsaude.newfeature.utils.SharedPreferences
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeleteUserFragment : Fragment() {
    private lateinit var binding: FragmentDeleteUserBinding

    private val viewModel: DeleteUserViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDeleteUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservables()
        setupListeners()
    }

    private fun setupViews() {
        (activity as DeleteUserActivity).showBackButton()
    }

    private fun setupListeners() {
        with(binding) {
            buttonDelete.setOnClickListener {
                deleteUser()
            }

        }
    }

    private fun setupObservables() {
        with(viewModel) {
            data.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        viewModel.logout()
                    }
                }
            }
            logout.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        activity?.moveTaskToBack(true)
                        activity?.finish()
                        viewModel.removeToken()
                    }
                }
            }
        }
    }

    private fun deleteUser(){
        context?.let {
            DialogHelper.showDialog(it,
                R.string.delete_profile_title_modal,
                R.string.delete_profile_modal_title,
                R.string.delete_profile_button_modal,
                R.string.delete_profile_button_cancel,
                { viewModel.deleteUser() })
        }
    }


    private fun showLoading() {
        with(binding) {
//            view_schedule_central.alpha = .1F
//            progress_bar_schedule_central.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
//            view_schedule_central.alpha = 1F
//            progress_bar_schedule_central.visibility = View.GONE
        }
    }

}