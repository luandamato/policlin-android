package br.com.policlinsaude.ui.activities.ownNetwork

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.ListItemOwnNetworkTabBinding

/**
 * Adapter de la tira de tabs (ciudades) de Rede Propia.
 *
 * Reemplazo al `<TabLayout>` del legado usando RecyclerView horizontal
 * (mismo patrón de los íconos de calificación). Una pestaña por ciudad;
 * al pulsar se informa el índice para cambiar de página en el ViewPager2.
 */
class OwnNetworkTabAdapter(
    private val context: Context,
    private val onTabClick: (Int) -> Unit
) : RecyclerView.Adapter<OwnNetworkTabAdapter.TabViewHolder>() {

    data class OwnNetworkTab(val title: String)

    private val tabs = mutableListOf<OwnNetworkTab>()
    private var selectedIndex = 0

    fun update(newTabs: List<OwnNetworkTab>, selected: Int) {
        tabs.clear()
        tabs.addAll(newTabs)
        selectedIndex = selected
        notifyDataSetChanged()
    }

    fun setSelected(selected: Int) {
        selectedIndex = selected
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = tabs.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val binding = ListItemOwnNetworkTabBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        holder.bind(tabs[position].title, position == selectedIndex)
    }

    inner class TabViewHolder(private val binding: ListItemOwnNetworkTabBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String, selected: Boolean) {
            binding.tabTitleTextView.text = title
            binding.tabTitleTextView.setTextColor(
                if (selected) {
                    ContextCompat.getColor(context, R.color.colorPrimary)
                } else {
                    ContextCompat.getColor(context, R.color.gray_4)
                }
            )
            binding.tabTitleTextView.setOnClickListener { onTabClick.invoke(getAdapterPosition()) }
        }
    }
}