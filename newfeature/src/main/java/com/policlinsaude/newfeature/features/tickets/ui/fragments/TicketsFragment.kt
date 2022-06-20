package com.policlinsaude.newfeature.features.tickets.ui.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentTicketsBinding
import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel
import com.policlinsaude.newfeature.features.tickets.ui.adapters.TicketAdapter
import com.policlinsaude.newfeature.features.tickets.ui.viewmodels.TicketViewModel
import kotlinx.coroutines.channels.ticker
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.policlinsaude.newfeature.features.tickets.data.models.TicketDetail
import com.policlinsaude.newfeature.features.tickets.ui.activities.TicketsActivity
import com.policlinsaude.newfeature.utils.*
import com.whiteelephant.monthpicker.MonthPickerDialog
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception


class TicketsFragment : Fragment() {

    private lateinit var binding: FragmentTicketsBinding

    private val viewModel: TicketViewModel by viewModel()

    private val adapter by lazy { TicketAdapter() }

    private var firstTicket: TicketDetail? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onGetTickets()
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
            linearLayoutFilter.setOnClickListener {
                showMenu(it, R.menu.tickets_filter_options)
            }

            sendButton.setOnClickListener {
                firstTicket?.let {
                    goToTicketDetail(it)
                }
            }

            adapter.setOnClickListener = {
                it?.let { detail ->
                    goToTicketDetail(detail)
                }
            }
        }
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
            textviewOpenTicket.text = ticket?.sdtBoleto?.get(0)?.vencimento?.toMMYYYY().orEmpty()

            textviewValueOpenTicket.text = ticket?.sdtBoleto?.get(0)?.valor?.toCurrencyBRL().orEmpty()
            textviewDueOpenTicket.text = ticket?.sdtBoleto?.get(0)?.vencimento?.toDDMMYYYY().orEmpty()

            adapter.update(ticket?.sdtBoleto)
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v, Gravity.CENTER, 0, R.style.MyPopupMenu)
        popup.menuInflater.inflate(menuRes, popup.menu)
        binding.root.alpha = .1F
        popup.setOnDismissListener { binding.root.alpha = 1F }
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId) {
                R.id.filter_open_payment -> { viewModel.onGetTickets(); true }
                R.id.filter_due_month_year -> {
                    context?.calendarMonthYear { month, year ->
                        viewModel.onGetTickets(option = 2, year = year, month = month)
                    }
                    true
                }
                R.id.filter_due_year -> {
                    context?.calendarYear { year ->
                        viewModel.onGetTickets(option = 3, year = year)
                    }
                    true
                }
                else -> { false }
            }
        }

        popup.show()
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


}
