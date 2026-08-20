package com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentGuideAuthorizerBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.activities.GuideAuthorizerActivity
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters.GuideAuthorizerAdapter
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.viewmodels.GuideAuthorizerViewModel
import com.policlinsaude.newfeature.utils.openBrowser
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GuideAuthorizerFragment : Fragment() {

    private lateinit var binding: FragmentGuideAuthorizerBinding

    private val adapter by lazy { GuideAuthorizerAdapter() }

    private val viewModel by sharedViewModel<GuideAuthorizerViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGuideAuthorizerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.isFromActivity)
            viewModel.onGetListGuideAuthorizer()

        setupViews()
        setupObservables()
        (activity as GuideAuthorizerActivity).showBackButton()
    }

    private fun setupViews() {
        with(binding) {
            adapter.isShowCheckbox(false)
            if(!viewModel.isFromActivity)
                showEditButton(true)
            recyclerview.adapter = adapter
            adapter.onItemsSelected = { isSelected ->
                showEditButton(!isSelected)
                showCancelButton(isSelected)
            }

            adapter.onClickCheckBox = { isRemove, item ->
                if(isRemove)
                    viewModel.removeSelectedGuideAuthorizer(item)
                else
                    viewModel.addSelectedGuideAuthorizer(item)

            }

            buttonProcessRequest.setOnClickListener {
                navigateToNewRequest()
            }

            buttonCancelGuideAuthorizer.setOnClickListener {
                showLoading()
                viewModel.onPostRequestGuideAuthorizerCancel()
            }

            consulting.setOnClickListener {
                context?.openBrowser("https://policlinsaude.com.br/infoAutorizador.html")
            }
        }
    }

    private fun navigateToNewRequest(){
        if (viewModel.isListBeneficiaryEnable()){
            viewModel.onGetDependents()
        }
        else{
            findNavController().navigate(GuideAuthorizerFragmentDirections.navigateToBeneficiaryData())
        }
    }

    private fun setupObservables() {
        with(viewModel) {
            guideAuthorizer.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        it.getData()?.let { guide ->
                            if(guide.sdtAutPesq.isNotEmpty()) {
                                with(adapter) {
                                    update(guide.sdtAutPesq)
                                    viewModel.isFromActivity = false
                                    setOnClickListener = {
                                        findNavController().navigate(GuideAuthorizerFragmentDirections.guideAuthorizerToRequestGuideAuthorizerDetails(it?.numeroWEB.orEmpty()))
                                    }
                                }
                                showEditButton(true)
                                binding.textviewEmpty.visibility = View.GONE
                            } else {
                                adapter.update(arrayListOf())
                                showEditButton(false)
                                binding.textviewEmpty.visibility = View.VISIBLE
                            }
                        }
                    }
                    else -> hideLoading()
                }
            }

            guideAuthorizerCancel.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        showCancelButton(false)
                        showSelectAll(false)
                        adapter.isShowCheckbox(false)
                        viewModel.guidAuthorizerSelectedClean()
                        viewModel.onGetListGuideAuthorizer()
                    }
                    else -> hideLoading()
                }
            }

            dependets.observe(viewLifecycleOwner){
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        it.getData()?.let { dependets ->
                            if (dependets.listaBeneficiario.isNullOrEmpty() || dependets.listaBeneficiario.size <= 1){
                                findNavController().navigate(GuideAuthorizerFragmentDirections.navigateToBeneficiaryData())
                            }
                            else{
                                findNavController().navigate(GuideAuthorizerFragmentDirections.navigateToBeneficiaryList())
                            }
                        }
                    }
                    else -> hideLoading()
                }
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            scrollViewIncomeTax.alpha = .1F
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
            scrollViewIncomeTax.alpha = 1F
            progressBar.visibility = View.GONE
        }
    }

    private fun showSelectAll(isShow: Boolean) {
        with(binding) {
            buttonCancelGuideAuthorizer.isVisible = isShow
            buttonProcessRequest.isVisible = !isShow
        }
    }

    private fun showEditButton(isShow: Boolean) {
        (activity as GuideAuthorizerActivity).showButtonEdit(isShow) {
            showSelectAll(isShow)
            adapter.isShowCheckbox(isShow)
        }
    }

    private fun showCancelButton(isShow: Boolean) {
        (activity as GuideAuthorizerActivity).showButtonCancel(isShow) {
            adapter.apply {
                isShowCheckbox(false)
                isSelectAll(false)
            }
            showSelectAll(false)

            showEditButton(verifyShowEditButton())
        }
    }

    private fun verifyShowEditButton() = !viewModel.guideAuthorizer.value?.getData()?.sdtAutPesq.isNullOrEmpty()

}