package br.com.policlinsaude.medicalGuideDetails.view.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

class TreeViewAdapter : BaseExpandableListAdapter() {

    private val groups: MutableList<String> = mutableListOf()
    private val children: MutableList<MutableList<String>> = mutableListOf()

    fun updateData(groups: List<String>, children: List<List<String>>) {
        this.groups.clear()
        this.groups.addAll(groups)
        this.children.clear()
        this.children.addAll(children.map { it.toMutableList() })
        notifyDataSetChanged()
    }

    override fun getGroupCount(): Int = groups.size

    override fun getChildrenCount(groupPosition: Int): Int {
        if (groupPosition !in groups.indices) return 0
        return children.getOrNull(groupPosition)?.size ?: 0
    }

    override fun getGroup(groupPosition: Int): Any = groups.getOrElse(groupPosition) { "" }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        val groupChildren = children.getOrNull(groupPosition) ?: return ""
        return groupChildren.getOrElse(childPosition) { "" }
    }

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds(): Boolean = false

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val textView = (convertView as? TextView) ?: TextView(parent.context)
        textView.text = getGroup(groupPosition).toString()
        textView.setPadding(48, 24, 48, 24)
        return textView
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val textView = (convertView as? TextView) ?: TextView(parent.context)
        textView.text = getChild(groupPosition, childPosition).toString()
        textView.setPadding(96, 16, 48, 16)
        return textView
    }
}