package piedel.piotr.thesis.ui.fragment.category.categorylist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.ui.fragment.category.categorylist.categoryviewholders.CategoryChildViewHolder
import piedel.piotr.thesis.ui.fragment.category.categorylist.categoryviewholders.CategoryGroupViewHolder


open class CategoryListAdapter constructor(var group: MutableList<CategoryExpandableGroup>)
    : ExpandableRecyclerViewAdapter<CategoryGroupViewHolder, CategoryChildViewHolder>(group) {

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): CategoryGroupViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.categories_list_expandable_group, parent, false)
        return CategoryGroupViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): CategoryChildViewHolder? {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.categories_list_expandable_item, parent, false)
        return CategoryChildViewHolder(view)
    }

    override fun onBindChildViewHolder(holder: CategoryChildViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?, childIndex: Int) {
        val childCategoryChild: CategoryChild = group?.items?.get(childIndex) as CategoryChild
        holder?.onBindChild(childCategoryChild)
    }

    override fun onBindGroupViewHolder(holder: CategoryGroupViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?) {
        val parentCategoryTitle = group?.title
        parentCategoryTitle?.let { holder?.onBindParent(it) }
    }

    fun updateList(passedList: MutableList<CategoryExpandableGroup>) {
        this.group.clear()
        this.group.addAll(passedList)
        notifyDataSetChanged()
    }
}