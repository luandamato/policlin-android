package br.com.policlinsaude.ui.fragments.tickets

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.TicketDetail
import br.com.policlinsaude.databinding.FragmentTicketsBinding
import br.com.policlinsaude.ui.activities.tickets.TicketsActivity
import br.com.policlinsaude.ui.dialogs.BottomSheetCommon
import br.com.policlinsaude.ui.dialogs.DialogHelper
import br.com.policlinsaude.util.extensions.getMonths
import br.com.policlinsaude.util.extensions.getYears
import br.com.policlinsaude.util.extensions.toMonthNumber
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Lista de boletos (2ª via) — MVVM.
 *
 * Migrado do legado `TicketsFragment` (stack `com.policlinsaude.newfeature`),
 * que já era MVVM; agora consome o [TicketViewModel] novo (LiveData +
 * SingleLiveEvent). A navegação para o detalhe é feita na UI via
 * [TicketsActivity.showDetail] (sem Navigation Component).
 */
class TicketsFragment : Fragment() {

    companion object {
        fun newInstance(): TicketsFragment = TicketsFragment()

        const val OPEN_BOTTOM_SHEET_YEAR = "OPEN_BOTTOM_SHEET_YEAR"
        const val OPEN_BOTTOM_SHEET_MONTH = "OPEN_BOTTOM_SHEET_MONTH"
    }

    private lateinit var binding: FragmentTicketsBinding

    private val viewModel: TicketViewModel by viewModel()

    private val adapter by lazy { TicketAdapter(requireContext()) }

    private var firstTicket: TicketDetail? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restaura dados já carregados (recriação da tela); senão busca na API.
        if (viewModel.tickets.value == null) {
            viewModel.onGetTickets()
        }

        viewModel.yearSelected?.let {
            ticketOpenedChangeUnSelect()
            showYear()
        }

        viewModel.monthYearSelected?.let {
            ticketOpenedChangeUnSelect()
            showYearAnMonth()
        }

        setupObservables()
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        with(binding) {
            recyclerViewTickets.adapter = adapter
        }

        (activity as TicketsActivity).showBackButton()
    }

    private fun setupListeners() {
        with(binding) {
            adapter.setOnClickListener = { detail ->
                detail?.let { goToTicketDetail(it) }
            }

            yearAndMonth.setOnClickListener {
                bottomSheetYearAndMonth()
            }

            year.setOnClickListener {
                bottomSheetYear()
            }

            ticketOpenedOption.setOnClickListener {
                ticketOpenedChangeSelect()
                viewModel.onGetTickets()
            }
        }
    }

    private fun bottomSheetYear() {
        BottomSheetCommon(
            title = getString(R.string.ticket_year),
            description = "Escolha o Ano que deseja filtrar",
            list = getYears(),
            onClickListenerNext = { selectedYear ->
                selectedYear?.let {
                    viewModel.yearSelected = it
                    ticketOpenedChangeUnSelect()
                    showYear()
                    viewModel.onGetTickets(option = 3, year = it.toInt())
                }
            },
            onClickListenerClean = {
                onClear()
            }
        ).show(childFragmentManager, OPEN_BOTTOM_SHEET_YEAR)
    }

    @SuppressLint("SetTextI18n")
    private fun bottomSheetYearAndMonth() {
        BottomSheetCommon(
            title = getString(R.string.ticket_year_month),
            description = "Escolha o ano e em seguida o mês",
            list = getYears(),
            buttonTitle = getString(R.string.next),
            onClickListenerNext = { year ->
                year?.let { selectedYear ->
                    BottomSheetCommon(
                        title = getString(R.string.ticket_year_month),
                        description = "Escolha o mês e aplique o filtro",
                        list = getMonths(selectedYear),
                        onClickListenerNext = { month ->
                            month?.let { selectedMonth ->
                                viewModel.monthYearSelected = "$selectedYear/${selectedMonth.substring(0, 3)}"
                                ticketOpenedChangeUnSelect()
                                showYearAnMonth()
                                viewModel.onGetTickets(
                                    option = 2,
                                    year = selectedYear.toInt(),
                                    month = selectedMonth.toMonthNumber()
                                )
                            }
                        },
                        onClickListenerClean = {
                            onClear()
                        }
                    ).show(childFragmentManager, OPEN_BOTTOM_SHEET_MONTH)
                }
            },
            onClickListenerClean = {
                onClear()
            }
        ).show(childFragmentManager, OPEN_BOTTOM_SHEET_MONTH)
    }

    private fun setupObservables() {
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) showLoading() else hideLoading()
        }

        viewModel.tickets.observe(viewLifecycleOwner) { list ->
            list?.let { success(it) }
        }

        viewModel.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                TicketEvent.Nothing -> Unit
                is TicketEvent.ShowError -> {
                    hideLoading()
                    adapter.update(arrayListOf())
                    DialogHelper.showErrorDialog(requireContext(), event.message)
                }
            }
        }
    }

    private fun success(ticketList: List<TicketDetail>) {
        firstTicket = ticketList.firstOrNull()
        adapter.update(ticketList.toMutableList())
    }

    private fun goToTicketDetail(detail: TicketDetail) {
        (activity as TicketsActivity).showDetail(detail)
    }

    private fun showLoading() {
        with(binding) {
            scrollViewTickets.alpha = .1F
            progressBarTickets.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        with(binding) {
            scrollViewTickets.alpha = 1F
            progressBarTickets.visibility = View.GONE
        }
    }

    private fun ticketOpenedChangeUnSelect() {
        with(binding) {
            ticketOpenedOption.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Branco))
            textviewTicketOpenned.setTextColor(ContextCompat.getColor(requireContext(), R.color.Cinza_Claro))
        }
    }

    private fun ticketOpenedChangeSelect() {
        with(binding) {
            ticketOpenedOption.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Bordo))
            textviewTicketOpenned.setTextColor(ContextCompat.getColor(requireContext(), R.color.Branco))
            onClearYear()
            onClearMonthAndYear()
        }
    }

    private fun showYearAnMonth() {
        onSelectedMonthAndYear()
        onClearYear()
    }

    private fun showYear() {
        onSelectYear()
        onClearMonthAndYear()
    }

    private fun onSelectYear() {
        ticketOpenedChangeUnSelect()
        with(binding) {
            year.apply {
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Bordo))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.Branco))
                post {
                    compoundDrawables.getOrNull(2)?.setTint(ContextCompat.getColor(requireContext(), R.color.Branco))
                }
                text = viewModel.yearSelected
            }
        }
    }

    private fun onSelectedMonthAndYear() {
        ticketOpenedChangeUnSelect()
        with(binding) {
            yearAndMonth.apply {
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Bordo))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.Branco))
                compoundDrawables.getOrNull(2)?.setTint(ContextCompat.getColor(requireContext(), R.color.Branco))
                text = viewModel.monthYearSelected
            }
        }
    }

    private fun onClearYear() {
        with(binding) {
            year.apply {
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Branco))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.Cinza_Claro))
                compoundDrawables.getOrNull(2)?.setTint(ContextCompat.getColor(requireContext(), R.color.Bordo))
                text = requireContext().getString(R.string.ticket_year)
            }
            viewModel.yearSelected = null
        }
    }

    private fun onClearMonthAndYear() {
        with(binding) {
            yearAndMonth.apply {
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Branco))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.Cinza_Claro))
                compoundDrawables.getOrNull(2)?.setTint(ContextCompat.getColor(requireContext(), R.color.Bordo))
                text = requireContext().getString(R.string.ticket_year_month)
            }
            viewModel.monthYearSelected = null
        }
    }

    private fun onClear() {
        ticketOpenedChangeSelect()
        viewModel.apply {
            onGetTickets()
            onClearSelected()
        }
    }
}