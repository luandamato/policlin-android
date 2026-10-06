package br.com.policlinsaude.ui.fragments.tickets

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
import android.widget.TextView.TEXT_ALIGNMENT_CENTER
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.TicketDetail
import br.com.policlinsaude.databinding.FragmentTicketDetailBinding
import br.com.policlinsaude.util.extensions.openBrowser
import br.com.policlinsaude.util.extensions.toCurrencyBRL
import br.com.policlinsaude.util.extensions.toDDMMYYYY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Detalhe de um boleto (2ª via) — MVVM.
 *
 * Migrado do legado `TicketDetailFragment`. No legado os dados vinham via
 * Navigation Component (`navArgs`/`TicketDetailFragmentArgs`); aqui o ticket é
 * passado pela UI ([TicketsActivity.showDetail]) por meio de [Bundle] de
 * argumentos (campos simples), preservando o comportamento de recriação
 * (arguments sobrevivem à rotação/recriação da view).
 */
class TicketDetailFragment : Fragment() {

    companion object {
        private const val ARG_VALOR = "valor"
        private const val ARG_VENCIMENTO = "vencimento"
        private const val ARG_INFORMACOES = "informacoes"
        private const val ARG_LINHA_DIGITAVEL = "linha_digitavel"
        private const val ARG_LINK = "link"

        fun newInstance(ticket: TicketDetail): TicketDetailFragment {
            val fragment = TicketDetailFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_VALOR, ticket.valor)
                putString(ARG_VENCIMENTO, ticket.vencimento)
                putString(ARG_INFORMACOES, ticket.informacoes)
                putString(ARG_LINHA_DIGITAVEL, ticket.linhaDigitavel)
                putString(ARG_LINK, ticket.link)
            }
            return fragment
        }
    }

    private lateinit var binding: FragmentTicketDetailBinding
    private lateinit var detail: TicketDetail

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTicketDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detail = TicketDetail(
            valor = arguments?.getString(ARG_VALOR),
            vencimento = arguments?.getString(ARG_VENCIMENTO),
            informacoes = arguments?.getString(ARG_INFORMACOES),
            linhaDigitavel = arguments?.getString(ARG_LINHA_DIGITAVEL),
            link = arguments?.getString(ARG_LINK)
        )
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        with(binding) {
            textviewValueTicketDetail.text = detail.valor?.toCurrencyBRL()
            textviewDueTicketDetail.text = String.format(
                getString(R.string.ticket_detail_due),
                detail.vencimento?.toDDMMYYYY()
            )
            textviewCodeTicketDetail.text = detail.linhaDigitavel

            if (detail.informacoes?.contains("Boleto pago") == true) {
                textviewCodeTicketDetail.text = detail.informacoes
                buttonCopyCodeTicketDetail.isGone = true
                linearLayoutDescriptionCodeBar.isGone = true
            } else {
                linearLayoutInfos.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ticket_detail_background_open)
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
                val clipboardManager =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
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