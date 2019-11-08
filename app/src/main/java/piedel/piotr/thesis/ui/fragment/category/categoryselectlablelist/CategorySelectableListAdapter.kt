package piedel.piotr.thesis.ui.fragment.category.categoryselectlablelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.category.categorychild.CategoryChild
import piedel.piotr.thesis.ui.fragment.category.categorylist.CategoryExpandableGroup
import piedel.piotr.thesis.ui.fragment.category.categorylist.CategoryListAdapter


class CategorySelectableListAdapter constructor(groupSelect: MutableList<CategoryExpandableGroup>) : CategoryListAdapter(groupSelect) {

    lateinit var categorySelectListAdapterOnClickHandler: CategorySelectListAdapterOnClickHandler

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): CategoryChildViewHolder? {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.categories_list_expandable_item, parent, false)
        return CategoryChildViewHolder(view)
    }

    override fun onBindChildViewHolder(holder: piedel.piotr.thesis.ui.fragment.category.categorylist.categoryviewholders.CategoryChildViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?, childIndex: Int) {
        val childCategoryChild: CategoryChild = group?.items?.get(childIndex) as CategoryChild
        holder?.childCategoryChild = childCategoryChild
        holder?.onBindChild(childCategoryChild)
    }

    fun setOnChildClickListener(categorySelectListAdapterOnClickHandler: CategorySelectListAdapterOnClickHandler) {
        this.categorySelectListAdapterOnClickHandler = categorySelectListAdapterOnClickHandler
    }

    interface CategorySelectListAdapterOnClickHandler {
        fun onChildClick(childCategoryChild: CategoryChild)
    }

    inner class CategoryChildViewHolder(itemView: View) : piedel.piotr.thesis.ui.fragment.category.categorylist.categoryviewholders.CategoryChildViewHolder(itemView) {
        init {
            itemView.setOnClickListener { childCategoryChild?.let { it1 -> categorySelectListAdapterOnClickHandler.onChildClick(it1) } }
        }
    }
}

