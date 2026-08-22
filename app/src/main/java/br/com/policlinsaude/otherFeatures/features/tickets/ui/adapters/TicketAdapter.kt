package com.policlinsaude.newfeature.features.tickets.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.AdapterTicketsBinding
import com.policlinsaude.newfeature.features.tickets.data.models.TicketDetail
import com.policlinsaude.newfeature.utils.toCurrencyBRL
import com.policlinsaude.newfeature.utils.toDDMMYYYY

class TicketAdapter(
    private val context: Context,
    private var list: MutableList<TicketDetail> = arrayListOf(),
    var setOnClickListener: (TicketDetail?) -> Unit? = { },
) : RecyclerView.Adapter<TicketAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketAdapter.ViewHolder {
        return ViewHolder(AdapterTicketsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TicketAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(data: MutableList<TicketDetail>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: AdapterTicketsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TicketDetail) {
            with(binding) {
                textviewDueDateTicket.text = item.vencimento?.toDDMMYYYY()
                textviewValueTicket.text = item.valor?.toCurrencyBRL()

                item.informacoes?.let {
                    when {
                        item.informacoes.contains(TICKET_OPEN) ->  {
                            buttonSeeDetails.apply {
                                background = ContextCompat.getDrawable(context, R.drawable.ticket_open)
                                text = item.informacoes //resources.getString(R.string.ticket_open)
                            }
                            textviewValueTicket.setTextColor(ContextCompat.getColor(context, R.color.Laranja))

                        }
                        item.informacoes.contains(TICKET_PAID) -> {
                            buttonSeeDetails.apply {
                                background = ContextCompat.getDrawable(context, R.drawable.ticket_paid)
                                text = item.informacoes //resources.getString(R.string.ticket_paid)
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