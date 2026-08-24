package com.policlinsaude.newfeature.features.extractor.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import br.com.policlinsaude.R
import com.policlinsaude.newfeature.components.bottomsheet.BottomSheetCommon
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import br.com.policlinsaude.databinding.FragmentFactorExtractorBinding
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorDetailItemsModel
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorDetailModel
import com.policlinsaude.newfeature.features.extractor.ui.activities.FactorExtractorActivity
import com.policlinsaude.newfeature.features.extractor.ui.adapters.FactorExtractorCardsAdapter
import com.policlinsaude.newfeature.features.extractor.ui.adapters.FactorExtractorDetailAdapter
import com.policlinsaude.newfeature.features.extractor.ui.viewmodels.FactorExtractorViewModel
import com.policlinsaude.newfeature.features.tickets.ui.fragments.TicketsFragment
import com.policlinsaude.newfeature.utils.DateMapper
import com.policlinsaude.newfeature.utils.DialogHelper
import com.policlinsaude.newfeature.utils.SharedPreferences
import com.policlinsaude.newfeature.utils.toCurrencyBRL
import com.policlinsaude.newfeature.utils.toMonths
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FactorExtractorFragment : Fragment() {

    private lateinit var binding: FragmentFactorExtractorBinding

    private val viewModel: FactorExtractorViewModel by sharedViewModel()

    private val adapter by lazy { FactorExtractorCardsAdapter(viewModel.isCoPartFm) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFactorExtractorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onGetFactorsExtractorsYears()
        viewModel.onGetUser()
        setupViews()
        setupListeners()
        setupObservables()
    }

    private fun setupViews() {
        (activity as FactorExtractorActivity).showBackButton()

        binding.textviewFeEmptyFilter .text = HtmlCompat.fromHtml(context?.getString(R.string.factor_extractor_empty_filter).orEmpty(), 0)
        showEmptyFilter()
        buttonIsEnabled()
        binding.recyclerViewFeItems.adapter = adapter
    }

    private fun setupListeners() {
        with(binding) {
            buttonFilter.setOnClickListener {
                viewModel.onGetFactorsExtractors()
            }

            buttonCleanFilter.setOnClickListener {
                hideCleanButton()
                clearMonth()
                clearYear()
                showEmptyFilter()
                isBlock()
            }

            textviewFeYear.setOnClickListener {
                with(viewModel) {
                    if(listYears.isNullOrEmpty())
                        onGetFactorsExtractorsYears()
                    else
                        bottomSheetYears(listYears)
                }
            }

            textviewFeMonth.setOnClickListener {
                with(viewModel) {
                    if(listMonths.isNullOrEmpty())
                        onGetFactorsExtractorsMonths()
                    else
                        bottomSheetMonths(listMonths)
                }
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
                                showEmptyFilter()
                            } else {
                                showListData()
                                binding.textviewTotalValue.text = it.getData()?.valorGeral?.toCurrencyBRL()
                                binding.textviewTotalDep.text = it.getData()?.valorTotalDep?.toCurrencyBRL()
                                binding.textviewTotalMat.text = it.getData()?.valorTotal?.toCurrencyBRL()
                                adapter.update(it.getData()?.extratoCopart ?: arrayListOf())
                                showCleanButton()
                            }
                            isBlock()
                        }

                    }
                    else -> {
                        hideLoading()
                    }
                }
            }
            user.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        binding.lblMatricula.text = "${it.getData()?.register}-${it.getData()?.order}"
                    }
                    else -> hideLoading()
                }
            }

            year.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        val years = DateMapper.toFactorExtractorYearsModelToYears(it.getData()?.sdtValores)
                        years?.let {
                            viewModel.listYears = years
                        }
                    }
                    else -> hideLoading()
                }
            }

            months.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        val months = DateMapper.toFactorExtractorMonthsModelToMonths(it.getData()?.sdtValores)
                        months?.let {
                            viewModel.listMonths = months
                        }
                    }
                    else -> hideLoading()
                }
            }
        }
    }

    private fun bottomSheetYears(years: MutableList<String>) {
        BottomSheetCommon(
            title = context?.getString(R.string.factor_extractor_select_year).orEmpty(),
            description = "Escolha o Ano que deseja filtrar",
            list = years,
            onClickListenerNext = { year ->
                year?.let {
                    with(viewModel) {
                        selectedYear = year
                        binding.textviewFeYear.text = year
                        onGetFactorsExtractorsMonths()
                    }
                    buttonIsEnabled()
                }
            },
            onClickListenerClean = {
                with(viewModel) {
                    clearYear()
                }
            }
        ).show(childFragmentManager, TicketsFragment.OPEN_BOTTOM_SHEET_YEAR)
    }

    private fun bottomSheetMonths(months: MutableList<String>) {
        BottomSheetCommon(
            title = context?.getString(R.string.factor_extractor_select_month).orEmpty(),
            description = "Escolha o mês que deseja filtrar",
            list = months,
            onClickListenerNext = { month ->
                month?.let {
                    with(viewModel) {
                        selectMonth = month.toMonths().toString()
                        binding.textviewFeMonth.text = month
                    }
                    buttonIsEnabled()
                }
            },
            onClickListenerClean = {
                with(viewModel) {
                    clearMonth()
                }
            }

        ).show(childFragmentManager, TicketsFragment.OPEN_BOTTOM_SHEET_YEAR)
    }

    private fun buttonIsEnabled() {
        with(binding) {
            if (viewModel.enableButton()) {
                buttonFilter.apply {
                    isEnabled = true
                    setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Bordo))
                }
            }
            else {
                buttonFilter.apply {
                    isEnabled = false
                    setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Bordo_10))
                    cardElevation = 0F
                }
            }
        }
    }

    private fun showEmptyFilter() {
        with(binding) {
            linearLayoutFeRecyclerView.isVisible = false
            cardViewTotal.isVisible = false
            linearLayoutFeEmptyFilter.isVisible = true
        }
    }

    private fun showListData() {
        with(binding) {
            linearLayoutFeRecyclerView.isVisible = true
            cardViewTotal.isVisible = true
            linearLayoutFeEmptyFilter.isVisible = false
        }
    }

    private fun clearYear() {
        viewModel.clearYearSelected()
        buttonIsEnabled()
        binding.textviewFeYear.text = viewModel.selectedYear
    }

    private fun clearMonth() {
        viewModel.clearMonthsSelected()
        buttonIsEnabled()
        binding.textviewFeMonth.text = viewModel.selectMonth
    }

    private fun showCleanButton() {
        with(binding) {
            buttonCleanFilter.isVisible = true
            buttonFilter.isVisible = false
        }
    }

    private fun hideCleanButton() {
        with(binding) {
            buttonCleanFilter.isVisible = false
            buttonFilter.isVisible = true
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

    private fun isBlock() {
        val mm = viewModel.extractor.value?.getData()
        with(binding) {
            if(textviewFeYear.text.isNullOrEmpty()) {
                textviewFeYear.isEnabled = true
                textviewFeMonth.isEnabled = true
            } else if(mm != null && mm.extratoCopart.isEmpty()) {
                textviewFeYear.isEnabled = true
                textviewFeMonth.isEnabled = true
            } else {
                textviewFeYear.isEnabled = false
                textviewFeMonth.isEnabled = false
            }
        }
    }

}