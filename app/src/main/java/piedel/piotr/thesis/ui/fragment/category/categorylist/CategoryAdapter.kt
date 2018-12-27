package piedel.piotr.thesis.ui.fragment.category.categorylist

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.pavlospt.roundedletterview.RoundedLetterView
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import piedel.piotr.thesis.R
import piedel.piotr.thesis.data.model.category.Category
import piedel.piotr.thesis.ui.fragment.category.categoryselectlist.CategoryExpandableGroup
import piedel.piotr.thesis.util.getRandomColor
import timber.log.Timber


class CategoryAdapter constructor(var group: MutableList<CategoryExpandableGroup>) : ExpandableRecyclerViewAdapter<CategoryGroupViewHolder, CategoryChildViewHolder>(group) {


    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): CategoryGroupViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.categories_list_expandable_group, parent, false)
        return CategoryGroupViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): CategoryChildViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.categories_list_expandable_item, parent, false)
        return CategoryChildViewHolder(view)
    }

    override fun onBindChildViewHolder(holder: CategoryChildViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?, childIndex: Int) {
        val childCategory: Category = group?.items?.get(childIndex) as Category
        holder?.onBindChild(childCategory)
    }

    override fun onBindGroupViewHolder(holder: CategoryGroupViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?) {
        val parentCategoryTitle = group?.title
        parentCategoryTitle?.let { holder?.onBindParent(it) }
    }

    fun updateList(passedList: MutableList<CategoryExpandableGroup>) {
        this.group.clear()
        this.group.addAll(passedList)
        Timber.d("updateList" + passedList.toString())
        notifyDataSetChanged()
    }

}

class CategoryGroupViewHolder(itemView: View) : GroupViewHolder(itemView) {
    private val parentCategoryGroupTitle = itemView.findViewById(R.id.categories_list_expandable_group_header) as TextView
    private val parentCategoryGroupTitleLetterView = itemView.findViewById(R.id.round_letter_view) as RoundedLetterView

    fun onBindParent(parentCategoryTitle: String) {
        parentCategoryGroupTitle.setTypeface(null, Typeface.BOLD)
        parentCategoryGroupTitle.text = parentCategoryTitle
        parentCategoryGroupTitleLetterView.titleText = parentCategoryTitle[0].toString()
        parentCategoryGroupTitleLetterView.backgroundColor = getRandomColor()
    }

}

class CategoryChildViewHolder(itemView: View) : ChildViewHolder(itemView) {

    private val childCategoryTitle = itemView.findViewById(R.id.categories_list_expandable_item_title) as TextView
    private val childCategoryTitleLetterView = itemView.findViewById(R.id.round_letter_view) as RoundedLetterView

    fun onBindChild(childCategory: Category) {
        childCategoryTitle.text = childCategory.category_title
        childCategoryTitleLetterView.titleText = childCategory.category_title[0].toString()
        childCategoryTitleLetterView.backgroundColor = getRandomColor()
    }
}

class CategoryExpandableGroup(praentCategory: Category, items: List<Category>) : ExpandableGroup<Category>(praentCategory.category_title, items)