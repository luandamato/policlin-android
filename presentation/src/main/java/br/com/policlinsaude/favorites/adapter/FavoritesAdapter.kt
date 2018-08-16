package br.com.policlinsaude.favorites.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.policlinsaude.R
import br.com.policlinsaude.model.PresentationEstablishment

/**
 * Created by lmiyagi on 12/04/18.
 */
class FavoritesAdapter(private val listener: FavoritesAdapter.OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_ITEM = 1
    private val TYPE_HEADER = 0

    private val favorites = ArrayList<FavoritesWrapper>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            FavoritesHeaderViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_item_favorite_header, parent, false))
        } else {
            return FavoritesItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_item_favorite_item, parent, false))
        }
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is FavoritesHeaderViewHolder) {
            holder.format(favorites[position])
        } else if (holder is FavoritesItemViewHolder) {
            holder.format(favorites[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (favorites[position].isHeader) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    fun setEstablishemtns(establishments: ArrayList<PresentationEstablishment>) {
        wrapFavorites(establishments)
        notifyDataSetChanged()
    }

    private fun wrapFavorites(establishments: ArrayList<PresentationEstablishment>) {
        favorites.clear()
        establishments.sortBy { it.speciality }
        establishments.forEachIndexed { index, it ->
            if (index == 0) {
                favorites.add(FavoritesWrapper(true, it.speciality))
            } else if (!it.speciality.equals(establishments[index - 1].speciality, true)) {
                favorites.add(FavoritesWrapper(true, it.speciality))
            }
            favorites.add(FavoritesWrapper(false, it.title, index))
        }
    }

    inner class FavoritesHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun format(favoritesWrapper: FavoritesWrapper) {
            itemView.findViewById<TextView>(R.id.titleTextView).text = favoritesWrapper.title
        }
    }

    inner class FavoritesItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun format(favoritesWrapper: FavoritesWrapper) {
            itemView.findViewById<TextView>(R.id.titleTextView).text = favoritesWrapper.title
            itemView.findViewById<ViewGroup>(R.id.favoriteItemContainer).setOnClickListener {
                favoritesWrapper.index?.let {
                    listener.onItemClick(it)
                }
            }
        }
    }

    inner class FavoritesWrapper(val isHeader: Boolean,
                                 val title: String,
                                 val index: Int? = null)

    interface OnItemClickListener {
        fun onItemClick(establishmentIndex: Int)
    }
}