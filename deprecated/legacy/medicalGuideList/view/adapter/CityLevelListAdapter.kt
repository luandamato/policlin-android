package br.com.policlinsaude.ui.legacy.medicalGuideList.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import br.com.policlinsaude.R




class CityLevelAdapter(private val context: Context, var expandableListView: ExpandableListView, var headers: MutableList<String>,  var data: MutableList<MutableList<String>>) : BaseExpandableListAdapter() {

    internal var ivGroupIndicator: ImageView? = null

    override fun getGroup(groupPosition: Int): String {

        return headers[groupPosition]
    }

    override fun getGroupCount(): Int {

        return headers.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.treeview_plan, null)
        }
        val text = convertView?.findViewById<TextView>(R.id.rowPlanText)

        text?.text = getGroup(groupPosition)
        text?.setOnClickListener {
            if (expandableListView.isGroupExpanded(groupPosition))
                expandableListView.collapseGroup(groupPosition)
            else
                expandableListView.expandGroup(groupPosition)

        Toast.makeText(context, getGroup(groupPosition),Toast.LENGTH_SHORT).show()
    }

        return convertView
    }

    override fun getChild(groupPosition: Int, childPosition: Int): String {

        return data[groupPosition][childPosition]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View? {

        var convertView = convertView
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.treeview_city, null)
        }
        val text = convertView?.findViewById<TextView>(R.id.rowCityText)

        text?.text = getChild(groupPosition,childPosition)
        text?.setOnClickListener {
            Toast.makeText(context, getChild(groupPosition,childPosition),Toast.LENGTH_SHORT).show()
        }

        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return data[groupPosition].size
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
