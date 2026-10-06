package br.com.policlinsaude.ui.fragments.tickets

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.R
import br.com.policlinsaude.data.models.TicketDetail
import br.com.policlinsaude.databinding.AdapterTicketsBinding
import br.com.policlinsaude.util.extensions.toCurrencyBRL
import br.com.policlinsaude.util.extensions.toDDMMYYYY

/**
 * Adapter da lista de boletos (2ª via).
 * Mantém o mesmo comportamento do legado: ícone + vencimento + status
 * (aberto/pago) + valor; clique no item ou no botão abre o detalhe.
 */
class TicketAdapter(
    private val context: Context,
    private var list: MutableList<TicketDetail> = arrayListOf(),
    var setOnClickListener: (TicketDetail?) -> Unit = {}
) : RecyclerView.Adapter<TicketAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            AdapterTicketsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(data: MutableList<TicketDetail>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterTicketsBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: TicketDetail) {
            with(binding) {
                textviewDueDateTicket.text = item.vencimento?.toDDMMYYYY()
                textviewValueTicket.text = item.valor?.toCurrencyBRL()

                item.informacoes?.let {
                    when {
                        item.informacoes.contains(TICKET_OPEN) -> {
                            buttonSeeDetails.apply {
                                background = ContextCompat.getDrawable(context, R.drawable.ticket_open)
                                text = item.informacoes
                            }
                            textviewValueTicket.setTextColor(ContextCompat.getColor(context, R.color.Laranja))
                        }
                        item.informacoes.contains(TICKET_PAID) -> {
                            buttonSeeDetails.apply {
                                background = ContextCompat.getDrawable(context, R.drawable.ticket_paid)
                                text = item.informacoes
                            }
                            textviewValueTicket.setTextColor(ContextCompat.getColor(context, R.color.Verde))
                        }
                    }
                }

                root.setOnClickListener {
                    setOnClickListener(item)
                }

                buttonSeeDetails.setOnClickListener {
                    setOnClickListener(item)
                }
            }
        }
    }

    companion object {
        const val TICKET_OPEN = "Boleto em aberto"
        const val TICKET_PAID = "Boleto pago"
    }
}