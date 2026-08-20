package com.policlinsaude.newfeature.components.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.BottomSheetItemBinding

class BottomSheetAdapter (
    private val context: Context,
    private var list: MutableList<String> = arrayListOf(),
    var onSelectItemListener: ((listener: String) -> Unit) = {}
) : RecyclerView.Adapter<BottomSheetAdapter.ViewHolder>() {

    private var selectedItemPosition: Int = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetAdapter.ViewHolder {
        return ViewHolder(
            BottomSheetItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BottomSheetAdapter.ViewHolder, position: Int) {
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

                if(selectedItemPosition == position) {
                    binding.root.setBackgroundColor(ContextCompat.getColor(context, R.color.Bordo))
                    textviewDescription.setTextColor(ContextCompat.getColor(context, R.color.Branco))
                } else {
                    binding.root.setBackgroundColor(ContextCompat.getColor(context, R.color.Branco))
                    textviewDescription.setTextColor(ContextCompat.getColor(context, R.color.black))
                }

                root.setOnClickListener {
                    selectedItemPosition = position;
                    onSelectItemListener.invoke(item)
                    notifyDataSetChanged()
                }
                textviewDescription.text = item
            }
        }
    }
}