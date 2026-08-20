package com.policlinsaude.newfeature.features.tickets.ui.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TextView.TEXT_ALIGNMENT_CENTER
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.FragmentTicketDetailBinding
import com.policlinsaude.newfeature.features.tickets.data.models.TicketDetail
import com.policlinsaude.newfeature.utils.openBrowser
import com.policlinsaude.newfeature.utils.toCurrencyBRL
import com.policlinsaude.newfeature.utils.toDDMMYYYY
import kotlinx.coroutines.*

class TicketDetailFragment : Fragment() {

    private lateinit var binding: FragmentTicketDetailBinding
    private lateinit var detail: TicketDetail

    private val args: TicketDetailFragmentArgs by navArgs()

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
            textviewValueTicketDetail.text = detail.valor?.toCurrencyBRL()
            textviewDueTicketDetail.text = String.format(getString(R.string.ticket_detail_due), detail.vencimento?.toDDMMYYYY())
            textviewCodeTicketDetail.text = detail.linhaDigitavel

            if(detail.informacoes?.contains("Boleto pago") == true) {
                textviewCodeTicketDetail.text = detail.informacoes
                buttonCopyCodeTicketDetail.isGone = true
                linearLayoutDescriptionCodeBar.isGone = true

            } else {
                linearLayoutInfos.background = ContextCompat.getDrawable(requireContext(), R.drawable.ticket_detail_background_open)
                textviewValueTicketDetail.setTextColor(ContextCompat.getColor(requireContext(), R.color.Laranja))
                textviewDueTicketDetail.setTextColor(ContextCompat.getColor(requireContext(), R.color.Laranja))
            }
            buttonSeePdfTicketDetail.isGone = detail.linhaDigitavel.isNullOrEmpty()
            buttonCopyCodeTicketDetailSuccess.apply {
                val img = ImageSpan(context, R.drawable.ic_baseline_check_24)
                val spannableText = SpannableString(context.getString(R.string.ticket_detail_copy_code))
                spannableText.setSpan(img, 0, 1, 2)
                textAlignment = TEXT_ALIGNMENT_CENTER
                gravity = Gravity.CENTER
                text = spannableText
            }
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
                transformButton(success = true, original = false)
                val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("", detail.linhaDigitavel)
                clipboardManager.setPrimaryClip(clipData)

                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        delay(2000)
                        transformButton(success = false, original = true)
                    }
                }
            }
        }
    }

    private fun transformButton(success: Boolean, original: Boolean) {
        binding.buttonCopyCodeTicketDetailSuccess.isVisible = success
        binding.buttonCopyCodeTicketDetail.isVisible = original
    }
}