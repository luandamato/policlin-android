package br.com.policlinsaude.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.databinding.ListItemHomeBinding
import br.com.policlinsaude.home.view.model.PresentationHomeOptionEnum

class HomeAdapter(
    var list: MutableList<PresentationHomeOptionEnum>? = arrayListOf(),
    private val onItemClickListener: OnItemClickListener,
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val option = list?.get(position)
        holder.bind(option)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ListItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun getItemCount(): Int = list?.size ?: 0

    fun removeTicket() {
        try {
            list?.remove(PresentationHomeOptionEnum.TICKET)
            notifyDataSetChanged()
        } catch (e: Exception) {
        }
    }

    fun removeExtracts() {
        try {
            list?.remove(PresentationHomeOptionEnum.RESEARCH_VALUES_CO_PARTICIPATION)
            list?.remove(PresentationHomeOptionEnum.FACTOR_EXTRACTOR)
            notifyDataSetChanged()
        } catch (e: Exception) {
        }
    }

    fun removeIncomeTax() {
        try {
            list?.remove(PresentationHomeOptionEnum.INCOME_TAX)
            notifyDataSetChanged()
        } catch (e: Exception) {
        }
    }

    fun removeIncomeSchedule() {
        try {
            list?.remove(PresentationHomeOptionEnum.SCHEDULE)
            notifyDataSetChanged()
        } catch (e: Exception) {
        }
    }

    fun removeAuthorizer() {
        try {
            list?.remove(PresentationHomeOptionEnum.GUIDE_AUTHORIZER)
            notifyDataSetChanged()
        } catch (e: Exception) {
        }
    }

    fun removeToken() {
        try {
            list?.remove(PresentationHomeOptionEnum.SERVICE_TOKEN)
            notifyDataSetChanged()
        } catch (e: Exception) {
        }
    }

    fun setup(items: MutableList<PresentationHomeOptionEnum>) {
        list?.clear()
        list?.addAll(items)
        notifyDataSetChanged()
    }

    inner class HomeViewHolder(private val binding: ListItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: PresentationHomeOptionEnum?) {
            binding.imageView.setImageDrawable(option?.let {
                ContextCompat.getDrawable(
                    itemView.context,
                    it.drawable
                )
            })
            binding.root.setOnClickListener {
                if (option != null) {
                    onItemClickListener.onItemClick(option)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(option: PresentationHomeOptionEnum)
    }
}
