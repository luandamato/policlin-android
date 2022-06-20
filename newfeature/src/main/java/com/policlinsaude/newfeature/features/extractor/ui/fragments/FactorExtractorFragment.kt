package com.policlinsaude.newfeature.features.extractor.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentFactorExtractorBinding
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorDetailItemsModel
import com.policlinsaude.newfeature.features.extractor.ui.activities.FactorExtractorActivity
import com.policlinsaude.newfeature.features.extractor.ui.viewmodels.FactorExtractorViewModel
import com.policlinsaude.newfeature.utils.DialogHelper
import com.policlinsaude.newfeature.utils.calendarMonth
import com.policlinsaude.newfeature.utils.calendarYear
import com.policlinsaude.newfeature.utils.getMonth
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.collections.ArrayList


class FactorExtractorFragment : Fragment() {

    private lateinit var binding: FragmentFactorExtractorBinding

    private val viewModel: FactorExtractorViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFactorExtractorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
        setupObservables()
    }

    private fun setupViews() {
        (activity as FactorExtractorActivity).showBackButton()
    }

    private fun setupListeners() {
        with(binding) {
            textviewMonthFactorExtractor.setOnClickListener {
                context?.calendarMonth { month ->
                    viewModel.month = month
                    binding.textviewMonthFactorExtractor.text = month.getMonth()
                }
            }

            textviewYearFactorExtractor.setOnClickListener {
                context?.calendarYear { year ->
                    viewModel.year = year
                    binding.textviewYearFactorExtractor.text = year.toString()
                }
            }

            buttonSearchFactorExtractor.setOnClickListener {
                viewModel.onGetFactorsExtractors()
            }

            buttonCleanFactorExtractor.setOnClickListener {
                viewModel.clearDates()
                binding.textviewYearFactorExtractor.text = getString(R.string.factor_extractor_select_year)
                binding.textviewMonthFactorExtractor.text = getString(R.string.factor_extractor_select_month)
            }
        }
    }

    private fun setupObservables() {
        with(viewModel) {
            extractor.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> {
                        showLoading()
                    }
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        it.getData()?.let { extractor ->
                            if(extractor.extratoCopart.isNullOrEmpty()) {
                                DialogHelper.showErrorDialog(requireContext(), extractor.msgExterna)
                            } else {
                                val details: ArrayList<FactorExtractorDetailItemsModel> = arrayListOf()
                                 it.getData()?.extratoCopart?.filter { element ->
                                    details.addAll(element.itens)
                                }
                                findNavController().navigate(FactorExtractorFragmentDirections.factorExtractorFragmentToFactorExtractorDetailFragment(
                                    details.toTypedArray()
                                ))
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
            progressBarTickets.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
            linearLayoutItems.alpha = 1F
            progressBarTickets.visibility = View.GONE
        }
    }

}