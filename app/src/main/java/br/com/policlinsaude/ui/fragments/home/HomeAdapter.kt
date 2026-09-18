package br.com.policlinsaude.ui.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.databinding.ListItemHomeBinding

/**
 * Adapter del grid de opciones (tiles) de la home.
 * Migrado de `_legacy/.../home/view/adapter/HomeAdapter.kt`.
 */
class HomeAdapter(
    var list: MutableList<HomeOptionEnum> = mutableListOf(),
    private val onItemClick: (HomeOptionEnum) -> Unit
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(list.getOrNull(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ListItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    fun removeTicket() {
        list.remove(HomeOptionEnum.TICKET)
        notifyDataSetChanged()
    }

    fun removeExtracts() {
        list.remove(HomeOptionEnum.RESEARCH_VALUES_CO_PARTICIPATION)
        list.remove(HomeOptionEnum.FACTOR_EXTRACTOR)
        notifyDataSetChanged()
    }

    fun removeIncomeTax() {
        list.remove(HomeOptionEnum.INCOME_TAX)
        notifyDataSetChanged()
    }

    fun removeIncomeSchedule() {
        list.remove(HomeOptionEnum.SCHEDULE)
        notifyDataSetChanged()
    }

    fun removeAuthorizer() {
        list.remove(HomeOptionEnum.GUIDE_AUTHORIZER)
        notifyDataSetChanged()
    }

    fun removeToken() {
        list.remove(HomeOptionEnum.SERVICE_TOKEN)
        notifyDataSetChanged()
    }

    fun setup(items: MutableList<HomeOptionEnum>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    inner class HomeViewHolder(private val binding: ListItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: HomeOptionEnum?) {
            binding.imageView.setImageDrawable(
                option?.let { ContextCompat.getDrawable(itemView.context, it.drawable) }
            )
            binding.root.setOnClickListener {
                option?.let(onItemClick)
            }
        }
    }
}