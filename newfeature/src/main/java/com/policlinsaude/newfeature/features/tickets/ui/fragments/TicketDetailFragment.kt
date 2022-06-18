package com.policlinsaude.newfeature.features.tickets.ui.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.FragmentTicketDetailBinding
import com.policlinsaude.newfeature.features.tickets.data.models.TicketDetail
import com.policlinsaude.newfeature.utils.openBrowser

import com.policlinsaude.newfeature.utils.toCurrencyBRL
import com.policlinsaude.newfeature.utils.toDDMMYYYY
import com.policlinsaude.newfeature.utils.toMMYYYY

class TicketDetailFragment : Fragment() {

    private lateinit var binding: FragmentTicketDetailBinding
    private lateinit var detail: TicketDetail

    private val args: TicketDetailFragmentArgs by navArgs<TicketDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTicketDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detail = args.detail
        setupViews()
        setupListeners()
    }

    private fun setupViews() {

        with(binding) {
            textviewDateTicketDetail.text = detail.vencimento?.toMMYYYY()
            textviewValueTicketDetail.text = detail.valor?.toCurrencyBRL()
            textviewDueTicketDetail.text = String.format(getString(R.string.ticket_detail_due), detail.vencimento?.toDDMMYYYY())
            textviewCodeTicketDetail.text = detail.linhaDigitavel
            linearLayoutItems.isVisible = !detail.linhaDigitavel.isNullOrEmpty()
        }
    }

    private fun setupListeners() {
        with(binding) {
            buttonSeePdfTicketDetail.setOnClickListener {
                detail.link?.let {
                    context?.openBrowser(it)
                }
            }

            buttonCopyCodeTicketDetail.setOnClickListener {
                val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("", detail.linhaDigitavel)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(requireContext(), getString(R.string.ticket_details_copy_code), Toast.LENGTH_SHORT).show()
            }
        }
    }
}