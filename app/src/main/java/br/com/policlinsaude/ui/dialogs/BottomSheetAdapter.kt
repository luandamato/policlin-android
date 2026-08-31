package br.com.policlinsaude.ui.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.BottomSheetItemBinding

/**
 * Adapter de listas de seleção para [BottomSheetCommon].
 * Migrado/adaptado de `_legacy/.../otherFeatures/components/bottomsheet/BottomSheetAdapter.kt`.
 */
class BottomSheetAdapter(
    private val context: Context,
    private var list: MutableList<String> = arrayListOf(),
    var onSelectItemListener: ((String) -> Unit) = {}
) : RecyclerView.Adapter<BottomSheetAdapter.ViewHolder>() {

    private var selectedItemPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(BottomSheetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(data: MutableList<String>?) {
        list = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: BottomSheetItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            with(binding) {
                if (selectedItemPosition == position) {
                    root.setBackgroundColor(ContextCompat.getColor(context, R.color.Bordo))
                    textviewDescription.setTextColor(ContextCompat.getColor(context, R.color.Branco))
                } else {
                    root.setBackgroundColor(ContextCompat.getColor(context, R.color.Branco))
                    textviewDescription.setTextColor(ContextCompat.getColor(context, R.color.black))
                }

                root.setOnClickListener {
                    selectedItemPosition = position
                    onSelectItemListener.invoke(item)
                    notifyDataSetChanged()
                }
                textviewDescription.text = item
            }
        }
    }
}