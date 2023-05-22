package com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.data.models.UserModel
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentBeneficiaryDataBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.viewmodels.GuideAuthorizerViewModel
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BeneficiaryDataFragment : Fragment() {

    private lateinit var binding: FragmentBeneficiaryDataBinding

    private val viewModel by sharedViewModel<GuideAuthorizerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBeneficiaryDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onGetUser()
        setupObservables()
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {

            editTextEmail.doAfterTextChanged {
                isEnabledButton()
            }

            editTextPhoneNumber.doAfterTextChanged {
                isEnabledButton()
            }

            buttonConfirm.setOnClickListener {
                viewModel.setUserProcessRequest(
                    name = textviewName.text.toString(),
                    phone = editTextPhoneNumber.text.toString(),
                    emailuser = editTextEmail.text.toString(),
                    semGes = if(editTextSemGest.text.isNullOrEmpty()) 0 else editTextSemGest.text.toString().toInt()
                )
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setupViews(user: UserModel?) {
        with(binding) {
            editTextEmail.setText(user?.email)
            editTextPhoneNumber.setText(user?.phone)
            textviewMatricula.text = user?.register
            textviewOrder.text = user?.order
            textviewName.text = user?.name
        }
    }

    private fun setupObservables() {
        with(viewModel) {
            user.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        it?.getData()?.let { data -> setupViews(data) }
                    }
                    ViewModelResponseStatus.FAILED -> hideLoading()
                }
            }
        }
    }

    private fun isEnabledButton() {
        binding.buttonConfirm.isEnabled = binding.editTextPhoneNumber.text.toString().isNotEmpty() && binding.editTextEmail.text.toString().isNotEmpty()
    }

    private fun showLoading() {
        with(binding) {
            nestedScroolView.alpha = .1F
            progressBarTickets.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
            nestedScroolView.alpha = 1F
            progressBarTickets.visibility = View.GONE
        }
    }

}