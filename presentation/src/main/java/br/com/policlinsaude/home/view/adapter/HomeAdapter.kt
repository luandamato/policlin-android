package br.com.policlinsaude.home.view.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.home.view.model.PresentationHomeOptionEnum
import kotlinx.android.synthetic.main.list_item_home.view.*
import java.lang.Exception

class HomeAdapter(
    var list: MutableList<PresentationHomeOptionEnum>? = arrayListOf(),
    private val onItemClickListener: OnItemClickListener,
): RecyclerView.Adapter<HomeViewHolder>() {

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val option = list?.get(position)
        holder.itemView.imageView.setImageDrawable(option?.let { ContextCompat.getDrawable(holder.itemView.context, it.drawable) })
        holder.itemView.setOnClickListener {
            if (option != null) {
                onItemClickListener.onItemClick(option)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder
            = HomeViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_home, parent, false))


    override fun getItemCount(): Int = list?.size ?: 0

    fun removeTicket() {
        try {
           list?.let {
                it.remove(PresentationHomeOptionEnum.TICKET)
           }

            list = list
            notifyDataSetChanged()

        } catch (e: Exception) {}
    }

    fun removeExtracts() {
        try {
            list?.let {
                it.remove(PresentationHomeOptionEnum.RESEARCH_VALUES_CO_PARTICIPATION)
                it.remove(PresentationHomeOptionEnum.FACTOR_EXTRACTOR)
            }
            list = list
            notifyDataSetChanged()
        } catch (e: Exception) {}
    }

    fun setup(items: MutableList<PresentationHomeOptionEnum>) {
        list?.clear()
        list?.addAll(items)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {

        fun onItemClick(option: PresentationHomeOptionEnum)
    }
}
