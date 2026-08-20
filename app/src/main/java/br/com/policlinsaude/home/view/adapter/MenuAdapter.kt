package br.com.policlinsaude.home.view.adapter

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import br.com.policlinsaude.R
import br.com.policlinsaude.home.view.model.PresentationMenuEnum


/**
 * Created by lmiyagi on 3/19/18.
 */
class MenuAdapter(private val mContext: Context,
                  @LayoutRes private val layout: Int,
                  private val values: Array<PresentationMenuEnum>,
                  private val listener: OnMenuItemClickListener)
    : ArrayAdapter<PresentationMenuEnum>(mContext, layout, values) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHoder: MenuViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(layout, parent, false)
            viewHoder = MenuViewHolder(view)
            view.tag = viewHoder
        } else {
            view = convertView
            viewHoder = view.tag as MenuViewHolder
        }

        viewHoder.format(values[position])

        return view
    }

    override fun getItem(position: Int): PresentationMenuEnum = values[position]

    override fun getCount(): Int = values.size

    override fun getItemId(position: Int): Long = values[position].ordinal.toLong()

    inner class MenuViewHolder(row: View?) {

        private var menuContainer: View? = null
        private var iconImageView: ImageView? = null
        private var titleTextView: TextView? = null

        init {
            row?.let {
                menuContainer = it.findViewById(R.id.menu_container)
                iconImageView = it.findViewById(R.id.menu_icon) as ImageView
                titleTextView = it.findViewById(R.id.menu_title) as TextView
            }
        }

        fun format(menuItem: PresentationMenuEnum) {
            titleTextView?.setText(menuItem.title)
            iconImageView?.setImageDrawable(ContextCompat.getDrawable(context, menuItem.icon))
            menuContainer?.setOnClickListener {
                listener.onClick(menuItem)
            }
        }
    }

    public interface OnMenuItemClickListener {
        fun onClick(menuItem: PresentationMenuEnum)
    }
}