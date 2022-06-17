package com.policlinsaude.newfeature.features.tickets.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.policlinsaude.newfeature.data.networking.ViewModelResponseStatus
import com.policlinsaude.newfeature.databinding.FragmentTicketsBinding
import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel
import com.policlinsaude.newfeature.features.tickets.ui.adapters.TicketAdapter
import com.policlinsaude.newfeature.features.tickets.ui.viewmodels.TicketViewModel
import kotlinx.coroutines.channels.ticker
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


class TicketsFragment : Fragment() {

    private lateinit var binding: FragmentTicketsBinding

    private val viewModel: TicketViewModel by sharedViewModel()

    private val adapter by lazy { TicketAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onGetTickets()
        setupObservables()
    }

    private fun setupObservables() {
        with(viewModel) {
            tickets.observe(viewLifecycleOwner) {
                when(it.getResponseStatus()) {
                    ViewModelResponseStatus.RUNNING -> { }
                    ViewModelResponseStatus.SUCCESS -> {
                        setupView(it.getData())
                    }
                }
            }
        }
    }

    private fun setupView(ticket: TicketModel?) {
        with(binding) {
            textviewOpenTicket.text = Date().toString()

            textviewValueOpenTicket.text = ticket?.sdtBoleto?.get(0)?.valor.orEmpty()
            textviewDueOpenTicket.text = ticket?.sdtBoleto?.get(0)?.vencimento.orEmpty()

            recyclerViewTickets.adapter = adapter
        }
    }

}