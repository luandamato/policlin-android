package com.policlinsaude.newfeature.features.tickets.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.policlinsaude.newfeature.databinding.AdapterTicketsBinding
import com.policlinsaude.newfeature.features.tickets.data.models.TicketDetail
import com.policlinsaude.newfeature.features.tickets.data.models.TicketModel

class TicketAdapter(
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
                textviewDueDateTicket.text = item.vencimento
                textviewValueTicket.text = item.valor
            }
        }
    }

}