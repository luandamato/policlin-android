package com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.data.models.UserModel
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentBeneficiaryDataBinding
import com.policlinsaude.newfeature.features.Token.models.BeneficiarioModel
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.activities.GuideAuthorizerActivity
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
        (activity as GuideAuthorizerActivity).showButtonEdit(false)
        (activity as GuideAuthorizerActivity).showButtonCancel(false)
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
                    name = textviewInterlocutor.text.toString(),
                    phone = editTextPhoneNumber.text.toString(),
                    emailuser = editTextEmail.text.toString(),
                    semGes = if(editTextSemGest.text.isNullOrEmpty()) 0 else editTextSemGest.text.toString().toInt(),
                    pOrdem = textviewOrder.text.toString().toInt()
                )
                findNavController().popBackStack()
                if (!viewModel.processInialized){
                    findNavController().navigate(GuideAuthorizerFragmentDirections.navigateToRequest())
                }
            }
        }
    }

    private fun setupViews(user: UserModel?) {
        val beneficiario = viewModel.beneficiario
        with(binding) {
            editTextEmail.setText(user?.email)
            editTextPhoneNumber.setText(user?.phone)
            textviewInterlocutor.text = user?.name
            textviewMatricula.text = beneficiario?.matricula
            textviewOrder.text = beneficiario?.ordem
            textviewName.text = beneficiario?.Nome_Beneficiario
        }
    }

    private fun setupObservables() {
        with(viewModel) {
            user.observe(viewLifecycleOwner) {
                when (it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        it?.getData()?.let { data ->
                            if (viewModel.beneficiario == null || viewModel.beneficiario?.Nome_Beneficiario.isNullOrEmpty()) {
                                viewModel.beneficiario = BeneficiarioModel(
                                    data?.register!!,
                                    data?.order!!,
                                    data?.name!!
                                )
                            }
                            setupViews(data)
                        }
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