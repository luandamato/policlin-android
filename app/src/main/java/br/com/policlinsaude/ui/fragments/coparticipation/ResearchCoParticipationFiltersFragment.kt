package br.com.policlinsaude.ui.fragments.coparticipation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.CoParticipationItemsModel
import br.com.policlinsaude.databinding.FragmentResearchCoParticipationFiltersBinding
import br.com.policlinsaude.ui.activities.coparticipation.ResearchCoParticipationActivity
import br.com.policlinsaude.ui.dialogs.BottomSheetCommon
import br.com.policlinsaude.ui.dialogs.DialogHelper
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Filtros da Pesquisa de Valores de Coparticipação (MVVM).
 *
 * Migrado de `_legacy/.../coparticipation/ui/fragments/ResearchCoParticipationFiltersFragment.kt`
 * (stack `com.policlinsaude.newfeature`). Compartilha o [CoParticipationViewModel] com a
 * Activity e o fragment de itens; navegação (Filtros → Itens) feita na UI/Activity.
 */
class ResearchCoParticipationFiltersFragment : Fragment() {

    companion object {
        private const val TAG_BOTTOM_SHEET = "bottom_sheet_co_participation"

        fun newInstance(): ResearchCoParticipationFiltersFragment = ResearchCoParticipationFiltersFragment()
    }

    private lateinit var binding: FragmentResearchCoParticipationFiltersBinding

    private val viewModel: CoParticipationViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResearchCoParticipationFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewModel.isEnabledButtonSearch()) {
            viewModel.onGetComboOne()
            viewModel.onGetComboTwo()
            // comboTrês mantido comentado (mesmo comportamento do legado)
        }

        isEnabledButton()
        isCleanFilterShow()
        setupViews()
        setupObservables()
        setupListeners()
    }

    private fun setupViews() {
        (activity as? ResearchCoParticipationActivity)?.showBackButton()

        with(binding) {
            textviewRcpSelectCode.text = viewModel.optionComboOneSelection
            textviewRcpSelectGroupParticipation.text = viewModel.optionComboTwoSelection
            textviewRcpSelectDescriptionGroupParticipation.text = viewModel.optionComboThreeSelection
            textAreaInformation.setText(viewModel.optionDescription)

            if (viewModel.isCoPartFm) {
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
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) showLoading() else hideLoading()
        }

        viewModel.comboOne.observe(viewLifecycleOwner) { coparticipation ->
            coparticipation?.let { data ->
                if (data.items.isNullOrEmpty()) {
                    DialogHelper.showErrorDialog(requireContext(), data.msgExterna)
                } else {
                    viewModel.itemsComboOne = data.items.descriptionList()
                }
            }
        }

        viewModel.comboTwo.observe(viewLifecycleOwner) { coparticipation ->
            coparticipation?.let { data ->
                if (data.items.isNullOrEmpty()) {
                    DialogHelper.showErrorDialog(requireContext(), data.msgExterna)
                } else {
                    viewModel.itemsComboTwo = data.items.descriptionList()
                }
            }
        }

        viewModel.comboThree.observe(viewLifecycleOwner) { coparticipation ->
            coparticipation?.let { data ->
                if (data.items.isNullOrEmpty()) {
                    DialogHelper.showErrorDialog(requireContext(), data.msgExterna)
                } else {
                    viewModel.itemsComboThree = data.items.descriptionList()
                }
            }
        }

        viewModel.itemsCoParticipation.observe(viewLifecycleOwner) { coparticipation ->
            coparticipation?.let { data ->
                if (data.items.isNullOrEmpty()) {
                    DialogHelper.showErrorDialog(requireContext(), data.msgExterna)
                } else {
                    (activity as? ResearchCoParticipationActivity)?.showItems(data.items)
                }
            }
        }
    }

    private fun bottomSheetComboOne(codes: MutableList<String>) {
        BottomSheetCommon(
            title = "Código",
            description = "Escolha um dos grupos abaixo",
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
        ).show(childFragmentManager, TAG_BOTTOM_SHEET)
    }

    private fun bottomSheetComboTwo(codes: MutableList<String>) {
        BottomSheetCommon(
            title = "Grupo de Coparticipação",
            description = "Escolha um dos grupos abaixo",
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
        ).show(childFragmentManager, TAG_BOTTOM_SHEET)
    }

    private fun bottomSheetComboThree(codes: MutableList<String>) {
        BottomSheetCommon(
            title = "Descrição do Grupo Coparticipação",
            description = "Escolha um dos grupos abaixo",
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
        ).show(childFragmentManager, TAG_BOTTOM_SHEET)
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
        binding.buttonSearch.isEnabled = viewModel.isEnabledButtonSearch()
    }

    private fun isCleanFilterShow() {
        // Mesmo comportamento do legado: botão "Limpar campos" sempre visível
        binding.cleanFilters.isVisible = true
    }
}

/**
 * Extrai as descrições dos itens do combo
 * (equivalente ao `DateMapper.toCoParticipationToItems` do legado).
 */
private fun ArrayList<CoParticipationItemsModel>.descriptionList(): MutableList<String> =
    map { it.description }.toMutableList()
