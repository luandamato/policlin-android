package com.policlinsaude.newfeature.features.incometax.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentIncomeTaxBinding
import com.policlinsaude.newfeature.features.incometax.data.models.IncomeTaxItemModel
import com.policlinsaude.newfeature.features.incometax.ui.activities.IncomeTaxActivity
import com.policlinsaude.newfeature.features.incometax.ui.adapters.IncomeTaxItemAdapter
import com.policlinsaude.newfeature.features.incometax.ui.viewmodels.IncomeTaxViewModel
import com.policlinsaude.newfeature.utils.openBrowser
import org.koin.androidx.viewmodel.ext.android.viewModel

class IncomeTaxFragment : Fragment() {

    private lateinit var binding: FragmentIncomeTaxBinding

    private val adapter by lazy { IncomeTaxItemAdapter() }

    private val viewModel: IncomeTaxViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentIncomeTaxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservables()

        viewModel.onGetIncomeTax()
    }

    private fun setupViews() {
        with(binding) {
            recyclerViewIncomeTax.adapter = adapter
        }

        (activity as IncomeTaxActivity).showBackButton()
    }

    private fun setupObservables() {
        with(viewModel) {
            incomeTax.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()

                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        it.getData()?.let { income ->
                            if(!income.listaIR.isNullOrEmpty()) {
                                success(income.listaIR)
                                binding.textviewMsgNone.visibility = View.GONE
                            } else {
                                adapter.update(arrayListOf())
                                binding.textviewMsgNone.visibility = View.VISIBLE
                            }
                        }
                    }
                    else -> binding.progressBarIncomeTax.visibility = View.GONE
                }
            }
        }
    }

    private fun success(incomesTax: ArrayList<IncomeTaxItemModel>) {
        with(adapter) {
            update(incomesTax)
            setOnClickListener = {
                it?.let { detail ->
                    context?.openBrowser("https://docs.google.com/gview?embedded=true&url=${detail.link.orEmpty()}")
                }
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            scrollViewIncomeTax.alpha = .1F
            progressBarIncomeTax.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
            scrollViewIncomeTax.alpha = 1F
            progressBarIncomeTax.visibility = View.GONE
        }
    }

}