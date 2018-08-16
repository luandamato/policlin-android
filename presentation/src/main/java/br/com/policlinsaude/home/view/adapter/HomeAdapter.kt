package br.com.policlinsaude.home.view.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.home.view.model.PresentationHomeOptionEnum
import kotlinx.android.synthetic.main.list_item_home.view.*

class HomeAdapter(private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<HomeViewHolder>() {

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val option = PresentationHomeOptionEnum.values()[position]
        holder.itemView.imageView
                .setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, option.drawable))
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(option)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder
            = HomeViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_home, parent, false))


    override fun getItemCount(): Int = PresentationHomeOptionEnum.values().size

    interface OnItemClickListener {

        fun onItemClick(option: PresentationHomeOptionEnum)
    }
}
