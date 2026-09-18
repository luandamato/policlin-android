package br.com.policlinsaude.ui.activities.home

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

/**
 * Adapter de la lista del menú lateral (drawer).
 * Migrado de `_legacy/.../home/view/adapter/MenuAdapter.kt`.
 */
class MenuAdapter(
    private val context: Context,
    @LayoutRes private val layout: Int,
    private val values: Array<MenuOptionEnum>,
    private val listener: OnMenuItemClickListener
) : ArrayAdapter<MenuOptionEnum>(context, layout, values) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: MenuViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(layout, parent, false)
            holder = MenuViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as MenuViewHolder
        }

        holder.format(values[position])

        return view
    }

    override fun getItem(position: Int): MenuOptionEnum = values[position]

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

        fun format(menuItem: MenuOptionEnum) {
            titleTextView?.setText(menuItem.title)
            iconImageView?.setImageDrawable(ContextCompat.getDrawable(context, menuItem.icon))
            menuContainer?.setOnClickListener {
                listener.onClick(menuItem)
            }
        }
    }

    interface OnMenuItemClickListener {
        fun onClick(menuItem: MenuOptionEnum)
    }
}