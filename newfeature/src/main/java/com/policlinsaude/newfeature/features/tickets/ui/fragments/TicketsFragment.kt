package com.policlinsaude.newfeature.features.tickets.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.components.bottomsheet.BottomSheetCommon
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentTicketsBinding
import com.policlinsaude.newfeature.features.tickets.data.models.TicketDetail
import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel
import com.policlinsaude.newfeature.features.tickets.ui.activities.TicketsActivity
import com.policlinsaude.newfeature.features.tickets.ui.adapters.TicketAdapter
import com.policlinsaude.newfeature.features.tickets.ui.viewmodels.TicketViewModel
import com.policlinsaude.newfeature.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class TicketsFragment : Fragment() {

    private lateinit var binding: FragmentTicketsBinding

    private val viewModel: TicketViewModel by viewModel()

    private val adapter by lazy { TicketAdapter(requireContext()) }

    private var firstTicket: TicketDetail? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tickets.value?.getData()?.let {
            success(it)
        } ?: viewModel.onGetTickets()

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

            adapter.setOnClickListener = {
                it?.let { detail ->
                    goToTicketDetail(detail)
                }
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
            title = context?.getString(R.string.ticket_year).orEmpty(),
            description = "Escolha o Ano que deseja filtrar",
            list = getYears(),
            onClickListenerNext = {
                it?.let {
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
            title = context?.getString(R.string.ticket_year_month).orEmpty(),
            description = "Escolha o ano e em seguida o mês",
            list = getYears(),
            buttonTitle = context?.getString(R.string.next),
            onClickListenerNext = {
                it?.let { year ->
                    BottomSheetCommon(
                        title = context?.getString(R.string.ticket_year_month).orEmpty(),
                        description = "Escolha o mês e aplique o filtro",
                        list = getMonths(year),
                        onClickListenerNext = { month ->
                            month?.let {
                                viewModel.monthYearSelected = "$year/${month.substring(0,3)}"
                                ticketOpenedChangeUnSelect()
                                showYearAnMonth()
                                viewModel.onGetTickets(option = 2, year = year.toInt(), month = month.toMonths())
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
        with(viewModel) {
            tickets.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> {
                        showLoading()
                    }
                    ViewModelResponseStatus.SUCCESS -> {
                        hideLoading()
                        it.getData()?.let { ticket ->
                            if(!ticket.sdtBoleto.isNullOrEmpty()) {
                                success(it.getData())
                            } else {
                                adapter.update(arrayListOf())
                                DialogHelper.showErrorDialog(requireContext(), ticket.msgExterna.orEmpty())
                            }
                        }

                    }
                    else -> {
                        binding.progressBarTickets.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun success(ticket: TicketModel?) {
        with(binding) {
            firstTicket = ticket?.sdtBoleto?.get(0)
            adapter.update(ticket?.sdtBoleto)
        }
    }

    private fun goToTicketDetail(ticket: TicketDetail) {
        findNavController().navigate(
            TicketsFragmentDirections.ticketsFragmentToTicketDetailFragment(ticket)
        )
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
                text = context.getString(R.string.ticket_year)
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
                text = context.getString(R.string.ticket_year_month)
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



    companion object {
        const val OPEN_BOTTOM_SHEET_YEAR = "OPEN_BOTTOM_SHEET_YEAR"
        const val OPEN_BOTTOM_SHEET_MONTH = "OPEN_BOTTOM_SHEET_MONTH"
    }


}
