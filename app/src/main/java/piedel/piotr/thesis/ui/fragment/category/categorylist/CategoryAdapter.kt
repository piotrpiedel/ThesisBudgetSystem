package piedel.piotr.thesis.ui.fragment.category.categorylist

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.category.Category
import javax.inject.Inject

class CategoryAdapter @Inject constructor() : BaseExpandableListAdapter() {

    private var categoryListHeader: MutableList<Category> = mutableListOf()

    private var listChildHashMap: MutableMap<Int, MutableList<Category>> = mutableMapOf()

    private fun setCategoryListHeader(categoryListOther: MutableList<Category>) {
        categoryListHeader.clear()
        categoryListHeader.addAll(categoryListOther)
    }

    private fun setListChildHashMap(categoryChildLisOther: MutableMap<Int, MutableList<Category>>) {
        listChildHashMap.clear()
        listChildHashMap.putAll(categoryChildLisOther)
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listChildHashMap[categoryListHeader[groupPosition].categoryId]?.size
                ?: 0 //TODO: do sprawdzenia czy po title czy po id;
    }

    override fun getGroup(groupPosition: Int): Any {
        return categoryListHeader[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return listChildHashMap[categoryListHeader[groupPosition].categoryId]?.get(childPosition)
                ?: 0
    }


    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ChildViewHolder
        val childCategory = getChild(groupPosition, childPosition) as Category
        if (view == null) {
            view = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.categories_list_expandable_item, parent, false) as View
            holder = ChildViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as ChildViewHolder
        }
        holder.childCategoryTitle.text = childCategory.category_title
        return view

    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val holder: GroupViewHolder
        val category = getGroup(groupPosition) as Category
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.categories_list_expandable_group, null) as View
            holder = GroupViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as GroupViewHolder
        }
        holder.parentCategoryGroupTitle.text = category.category_title
        val listTitleTextView = view.findViewById<TextView>(R.id.categories_list_expandable_group_header)
        listTitleTextView?.setTypeface(null, Typeface.BOLD)
        listTitleTextView?.text = category.category_title
        return view
    }

    override fun areAllItemsEnabled(): Boolean {
        return false
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return categoryListHeader.size
    }

    fun updateList(listCategories: List<Category>, listChildHashMap: MutableMap<Int, MutableList<Category>>) {
        setCategoryListHeader(listCategories as MutableList<Category>)
        setListChildHashMap(listChildHashMap)
        notifyDataSetChanged()
    }

    internal class GroupViewHolder(view: View) {
        //        TODO: add ImageView with FirstCapitalLetter
        val parentCategoryGroupTitle = view.findViewById(R.id.categories_list_expandable_group_header) as TextView
    }

    internal class ChildViewHolder(view: View) {
        //        TODO: add ImageView with FirstCapitalLetter
        val childCategoryTitle = view.findViewById(R.id.categories_list_expandable_item_title) as TextView
    }
}