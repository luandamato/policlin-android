package com.policlinsaude.newfeature.features.guidAuthorizer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentConsultingBinding
import com.policlinsaude.newfeature.databinding.FragmentRequestDataBinding
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters.AdapterConsulting
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.adapters.ProcessRequestAdapter
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.viewmodels.GuideAuthorizerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ResponseDeadlinesFragment: Fragment() {

    private lateinit var binding: FragmentConsultingBinding
    private val viewModel by sharedViewModel<GuideAuthorizerViewModel>()
    private val adapter by lazy { AdapterConsulting() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConsultingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onPostRequestDeadlines()
        setupObservables()
        setupViews()
    }

    private fun setupViews() {
        binding.recyclerview.adapter = adapter
    }

    private fun setupObservables() {
        with(viewModel) {
            deadlines.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        binding.textviewDescription.text = it.getData()?.sdtAutPrazoResposta?.descricao
                        adapter.update(it.getData()?.sdtAutPrazoResposta?.prazos)
                        hideLoading()
                    }
                    ViewModelResponseStatus.FAILED -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            linearItems.alpha = .1F
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
            linearItems.alpha = 1F
            progressBar.visibility = View.GONE
        }
    }
}