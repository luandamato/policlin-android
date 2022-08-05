package com.policlinsaude.newfeature.features.coparticipation.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentResearchCoParticipationFiltersBinding
import com.policlinsaude.newfeature.features.coparticipation.ui.activities.ResearchCoParticipationActivity
import com.policlinsaude.newfeature.features.coparticipation.ui.viewmodels.CoParticipationViewModel
import com.policlinsaude.newfeature.features.tickets.ui.fragments.TicketsFragment
import com.policlinsaude.newfeature.components.bottomsheet.BottomSheetCommon
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationItemsDetails
import com.policlinsaude.newfeature.features.extractor.data.models.FactorExtractorDetailItemsModel
import com.policlinsaude.newfeature.utils.DateMapper
import com.policlinsaude.newfeature.utils.DialogHelper
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ResearchCoParticipationFiltersFragment : Fragment() {

    private lateinit var binding: FragmentResearchCoParticipationFiltersBinding

    private val viewModel: CoParticipationViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentResearchCoParticipationFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!viewModel.isEnabledButtonSearch()) {
            viewModel.onGetComboOne()
            viewModel.onGetComboTwo()
            //viewModel.onGetComboThree()
        }

        isEnabledButton()
        isCleanFilterShow()
        setupViews()
        setupObservables()
        setupListeners()
    }

    private fun setupViews() {
        (activity as ResearchCoParticipationActivity).showBackButton()

        with(binding) {
            textviewRcpSelectCode.text = viewModel.optionComboOneSelection
            textviewRcpSelectGroupParticipation.text = viewModel.optionComboTwoSelection
            textviewRcpSelectDescriptionGroupParticipation.text = viewModel.optionComboThreeSelection
            textAreaInformation.setText(viewModel.optionDescription)

            if(viewModel.isCoPartFm) {
                textviewRcpSelectGroupParticipation.visibility = View.GONE
                textviewTitleRcpSelectGroupParticipation.visibility = View.GONE
                textviewRcpSelectDescriptionGroupParticipation.visibility = View.GONE
                textviewTitleRcpSelectDescriptionGroupParticipation.visibility = View.GONE
                view1.visibility = View.GONE
                view2.visibility = View.GONE
            }
        }
    }

    private fun setupListeners() {
        with(binding) {
            textviewRcpSelectCode.setOnClickListener {
                viewModel.itemsComboOne?.let {
                    bottomSheetComboOne(it)
                }
            }

            textviewRcpSelectGroupParticipation.setOnClickListener {
                viewModel.itemsComboTwo?.let {
                    bottomSheetComboTwo(it)
                }
            }

            textviewRcpSelectDescriptionGroupParticipation.setOnClickListener {
                viewModel.itemsComboThree?.let {
                    bottomSheetComboThree(it)
                }
            }

            buttonSearch.setOnClickListener {
                viewModel.optionDescription = textAreaInformation.text.toString()
                viewModel.onGetItemCoParticipation()
            }

            textAreaInformation.doAfterTextChanged {
                viewModel.optionDescription = it.toString()
                isEnabledButton()
                isCleanFilterShow()
            }

            cleanFilters.setOnClickListener {
                clearComboOne()
                clearComboTwo()
                clearComboThree()
                textAreaInformation.text = null
                isCleanFilterShow()
            }
        }
    }

    private fun setupObservables() {
        with(viewModel) {
            comboOne.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        it.getData()?.let { coparticipation ->
                            if(coparticipation.items.isNullOrEmpty()) {
                                DialogHelper.showErrorDialog(requireContext(), coparticipation.msgExterna)
                            } else {
                                itemsComboOne = DateMapper.toCoParticipationToItems(it.getData()?.items)
                            }
                        }
                    }
                    else -> hideLoading()
                }
            }

            comboTwo.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        it.getData()?.let { coparticipation ->
                            if(coparticipation.items.isNullOrEmpty()) {
                                DialogHelper.showErrorDialog(requireContext(), coparticipation.msgExterna)
                            } else {
                                itemsComboTwo = DateMapper.toCoParticipationToItems(it.getData()?.items)
                            }
                        }
                    }
                    else -> hideLoading()
                }
            }

            comboThree.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> showLoading()
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        it.getData()?.let { coparticipation ->
                            if(coparticipation.items.isNullOrEmpty()) {
                                DialogHelper.showErrorDialog(requireContext(), coparticipation.msgExterna)
                            } else {
                                itemsComboThree = DateMapper.toCoParticipationToItems(it.getData()?.items)
                            }
                        }
                    }
                    else -> hideLoading()
                }
            }

            itemsCoParticipation.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> {
                        showLoading()
                    }
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        it.getData()?.let { coparticipation ->
                            if(coparticipation.items.isNullOrEmpty()) {
                                DialogHelper.showErrorDialog(requireContext(), coparticipation.msgExterna)
                            } else {
                                val details: ArrayList<CoParticipationItemsDetails> = it.getData()?.items?.map { element ->
                                    element
                                }?.toCollection(ArrayList()) ?: arrayListOf()

                                findNavController().navigate(
                                    ResearchCoParticipationFiltersFragmentDirections
                                        .researchCoParticipationFiltersFragmentToResearchCoParticipationItemsFragment(details.toTypedArray())
                                )
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

    private fun bottomSheetComboOne(codes: MutableList<String>) {
        BottomSheetCommon(
            title = "Código",
            description = "Escolha o mês um dos grupos abaixo",
            list = codes,
            onClickListenerNext = {
                binding.textviewRcpSelectCode.text = it
                viewModel.optionComboOneSelection = it
                isEnabledButton()
                isCleanFilterShow()
            },
            onClickListenerClean = {
                clearComboOne()
            }

        ).show(childFragmentManager, TicketsFragment.OPEN_BOTTOM_SHEET_YEAR)
    }

    private fun bottomSheetComboTwo(codes: MutableList<String>) {
        BottomSheetCommon(
            title = "Grupo de Coparticipação",
            description = "Escolha o mês um dos grupos abaixo",
            list = codes,
            onClickListenerNext = {
                binding.textviewRcpSelectGroupParticipation.text = it
                viewModel.optionComboTwoSelection = it
                isEnabledButton()
                isCleanFilterShow()
            },
            onClickListenerClean = {
                clearComboTwo()
            }

        ).show(childFragmentManager, TicketsFragment.OPEN_BOTTOM_SHEET_YEAR)
    }

    private fun bottomSheetComboThree(codes: MutableList<String>) {
        BottomSheetCommon(
            title = "Descrição do Grupo Coparticipação",
            description = "Escolha o mês um dos grupos abaixo",
            list = codes,
            onClickListenerNext = {
                binding.textviewRcpSelectDescriptionGroupParticipation.text = it
                viewModel.optionComboThreeSelection = it
                isEnabledButton()
                isCleanFilterShow()
            },
            onClickListenerClean = {
                clearComboThree()
            }

        ).show(childFragmentManager, TicketsFragment.OPEN_BOTTOM_SHEET_YEAR)
    }

    private fun clearComboOne() {
        viewModel.onClearSelectedComboOne()
        binding.textviewRcpSelectCode.text = ""
        isEnabledButton()
        isCleanFilterShow()
    }

    private fun clearComboTwo() {
        viewModel.onClearSelectedComboTwo()
        binding.textviewRcpSelectGroupParticipation.text = ""
        isEnabledButton()
        isCleanFilterShow()
    }

    private fun clearComboThree() {
        viewModel.onClearSelectedComboThree()
        binding.textviewRcpSelectDescriptionGroupParticipation.text = ""
        isEnabledButton()
        isCleanFilterShow()
    }

    private fun showLoading() {
        with(binding) {
            nestedScroolViewRcf.alpha = .1F
            progressBarTickets.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
            nestedScroolViewRcf.alpha = 1F
            progressBarTickets.visibility = View.GONE
        }
    }

    private fun isEnabledButton() {
        if(viewModel.isEnabledButtonSearch()) {
            binding.buttonSearch.apply {
                isEnabled = true
            }
        } else {
            binding.buttonSearch.apply {
                isEnabled = false
            }
        }
    }

    private fun isCleanFilterShow() {
        binding.cleanFilters.isVisible = true//!binding.textAreaInformation.text.isNullOrEmpty() || viewModel.isEnabledButtonSearch()
    }

}